package com.glens.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.glens.utils.ConvertUtils;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yk
 * @date 2018/3/8
 */
@RestController
@RequestMapping(value = "/models")
public class ModelerController {
    private static final String DEL = "del";
    private static Logger logger = LoggerFactory.getLogger(ExcelController.class);
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    ObjectMapper objectmapper;

    /**
     * 新建一个空模型
     *
     * @throws UnsupportedEncodingException 异常
     */
    @RequestMapping("newModel")
    public void newModel(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        //初始化一个空模型
        Model model = repositoryService.newModel();

        //设置一些默认信息
        String name = "new-process";
        String description = "";
        int revision = 1;
        String key = "process";

        ObjectNode modelNode = objectmapper.createObjectNode();
        modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        modelNode.put(ModelDataJsonConstants.MODEL_REVISION, revision);

        model.setName(name);
        model.setKey(key);
        model.setMetaInfo(modelNode.toString());

        repositoryService.saveModel(model);
        String id = model.getId();

        //完善ModelEditorSource
        ObjectNode editorNode = objectmapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectmapper.createObjectNode();
        stencilSetNode.put("namespace",
                "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.putPOJO("stencilset", stencilSetNode);
        repositoryService.addModelEditorSource(id, editorNode.toString().getBytes("utf-8"));
        try {
            response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到所有用户信息
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "listAllModel")
    public ModelAndView listAllModel(Map<String, Object> map) {
        List<Model> modelList = repositoryService.createModelQuery().list();
        map.put("ALLMODEL", modelList);
        return new ModelAndView("allModel");
    }

    /**
     * 获取分页起始条数
     *
     * @param request 传递的查询参数
     */
    @RequestMapping(value = "listModel", method = {RequestMethod.GET})
    public void listModel(HttpServletRequest request, HttpServletResponse response) {
        String strResult = "{\"total\":0,\"records\":0,\"rows\":[]}";
        HashMap<String, Object> result = new HashMap<>(3);
        int rows = ConvertUtils.objToInteger(request.getParameter("rows"));
        int page = ConvertUtils.objToInteger(request.getParameter("page"));
        int pageStart = (page - 1) * rows;
        try {
            List<Model> modelList = repositoryService.createModelQuery().listPage(pageStart, rows);
            Long count = repositoryService.createModelQuery().count();
            result.put("rows", modelList);
            result.put("records", ConvertUtils.objToInteger(count));
            result.put("total", Math.ceil(ConvertUtils.objToDouble(count) / rows));
            if (!result.isEmpty()) {
                strResult = JSON.toJSONString(result);
            }
        } catch (Exception e) {
            logger.error("[listModel] Exception: " + e.getMessage());
        }
        try {
            response.getWriter().write(strResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除模型
     */
    @RequestMapping(value = "delete", method = {RequestMethod.POST} )
    public void deleteModel(@RequestParam("id") String modelId,@RequestParam("oper") String oper) {
        if (DEL.equals(oper)){
            repositoryService.deleteModel(modelId);
        }
        /**
         * 级联删除
         * 	  不管流程是否启动，都能可以删除
         */

        System.out.println("删除成功！");
    }

    @RequestMapping(value = "deploy/{modelId}")
    public ModelAndView deploy(@PathVariable("modelId") String modelId, RedirectAttributes redirectAttributes) {
        try {
            Model modelData = repositoryService.getModel(modelId);
            ObjectNode modelNode = (ObjectNode) new ObjectMapper()
                    .readTree(repositoryService.getModelEditorSource(modelData.getId()));
            byte[] bpmnBytes;

            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            bpmnBytes = new BpmnXMLConverter().convertToXML(model);

            String processName = modelData.getName() + ".bpmn20.xml";

            Deployment deployment = repositoryService.createDeployment()
                    .name(modelData.getName()).addString(processName, new String(bpmnBytes, "UTF-8"))
                    .deploy();
            redirectAttributes.addFlashAttribute("message", "部署成功，部署ID=" + deployment.getId());
        } catch (Exception e) {
            logger.error("根据模型部署流程失败：modelId={}", modelId, e);
        }
        return new ModelAndView("redirect:http://localhost:8080/maven-demo/processModel.html");
    }
}

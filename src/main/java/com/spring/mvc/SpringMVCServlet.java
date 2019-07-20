package com.spring.mvc;

import com.spring.annoation.Controller;
import com.spring.annoation.RequestMapping;
import com.spring.utils.SpringMVCFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

public class SpringMVCServlet extends HttpServlet {


    @Override
    public void init() throws ServletException {
        super.init();
        String path = this.getServletContext().getRealPath("") + "WEB-INF\\classes\\";
        File file = new File(path);
        List<String> fileNameList = new ArrayList<>();
        getFile(fileNameList, file);
        fileNameList.forEach(s -> {
            try {
                String[] fileName = s.replace(path, "").split("\\\\");
                Class<?> fileClass = Class.forName(String.join(".", fileName).replace(".class", ""));
                if(isController(fileClass)){
                    RequestMapping controllerMapping = fileClass.getAnnotation(RequestMapping.class);
                    String controllerUrl = "";
                    if(controllerMapping != null){
                        controllerUrl = controllerMapping.value();
                    }
                    Method[] methods = fileClass.getDeclaredMethods();
                    for (Method method : methods) {
                        RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);
                        if(methodMapping != null){
                            SpringMVCFactory.putController(controllerUrl + methodMapping.value(), fileClass);
                            SpringMVCFactory.putMethod(controllerUrl + methodMapping.value(), method);
                        }
                    }
                }
            }catch (ClassNotFoundException e){

            }
        });
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI().replace(req.getContextPath(),"");
        Class controller = SpringMVCFactory.getController(url);
        Method method = SpringMVCFactory.getMethod(url);
        try {
            Object result = method.invoke(controller.newInstance());
            ServletOutputStream os = resp.getOutputStream();
            os.write(String.valueOf(result).getBytes());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private boolean isController(Class<?> fileClass){
        return fileClass.isAnnotationPresent(Controller.class);
    }

    private void getFile(List<String> list, File file){
        File[] files = file.listFiles();
        if(files != null){
            for (File f : files) {
                if(f.isFile()){
                    list.add(f.getAbsolutePath());
                }else if(f.isDirectory()){
                    getFile(list, f);
                }
            }
        }

    }
}

package org.bpmn;
import java.io.IOException;
import java.util.logging.Logger;
import org.camunda.bpm.client.ExternalTaskClient;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

public class ParamedicsHandler {
    private final static Logger LOGGER = Logger.getLogger(ParamedicsHandler.class.getName());
    public static void main(String[] args) {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest") //流程运行地址
                .asyncResponseTimeout(5000) //轮询间隔
                .build();

        client.subscribe("Reach patient") .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    LOGGER.info("Paramedics:Patient reached");
                    externalTaskService.complete(externalTask);

                })
                .open();

        client.subscribe("Evaluate patient conditions") .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    LOGGER.info("Paramedics:Evaluated patient conditions");
                    externalTaskService.complete(externalTask);

                })
                .open();

        client.subscribe("Blood collection By Paramedics") .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    LOGGER.info("Paramedics:Blood collection");
                    externalTaskService.complete(externalTask);

                })
                .open();

        client.subscribe("Blood collected by paramedics") .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {

                    String messageName = "Blood collected by paramedics";
                    String businessKey = externalTask.getBusinessKey();
                    String messageJson = String.format("{\"messageName\":\"%s\" , \"businessKey\" : \"%s\"}", messageName,businessKey);

                    try {
                        Request.Post("http://localhost:8080/engine-rest/message")
                                .bodyString(messageJson, ContentType.APPLICATION_JSON)
                                .execute()
                                .discardContent();
                        LOGGER.info("Paramedics:Send Msg 'Blood collected by paramedics'");
                        // 完成任务
                        externalTaskService.complete(externalTask);
                    } catch (IOException e) {
                        // 处理异常
                        externalTaskService.handleFailure(externalTask, e.getMessage(), "Failed to send message", 0, 5000);
                    }

                })
                .open();

        client.subscribe("Reach hospital") .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    LOGGER.info("Paramedics:Reach hospital");
                    externalTaskService.complete(externalTask);

                })
                .open();

        client.subscribe("Patient arrives at the hospital") .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    String businessKey = externalTask.getBusinessKey();
                    String messageName1 = "Patient arrives at the hospital to Operator";
                    String messageName2 = "Patient arrives at the hospital to Nurse";

                    String messageJson1 = String.format("{\"messageName\":\"%s\" , \"businessKey\" : \"%s\"}", messageName1,businessKey);
                    String messageJson2 = String.format("{\"messageName\":\"%s\" , \"businessKey\" : \"%s\"}", messageName2,businessKey);


                    try {
                        Request.Post("http://localhost:8080/engine-rest/message")
                                .bodyString(messageJson1, ContentType.APPLICATION_JSON)
                                .execute()
                                .discardContent();
                        LOGGER.info("Paramedics:Send Msg 'Patient arrives at the hospital to Operator'");

                        Request.Post("http://localhost:8080/engine-rest/message")
                                .bodyString(messageJson2, ContentType.APPLICATION_JSON)
                                .execute()
                                .discardContent();
                        LOGGER.info("Paramedics:Send Msg 'Patient arrives at the hospital to Nurse'");
                        // 完成任务
                        externalTaskService.complete(externalTask);
                    } catch (IOException e) {
                        // 处理异常
                        externalTaskService.handleFailure(externalTask, e.getMessage(), "Failed to send message", 0, 5000);
                    }

                })
                .open();
    }
}


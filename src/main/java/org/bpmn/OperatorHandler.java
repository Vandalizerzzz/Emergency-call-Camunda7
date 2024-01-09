package org.bpmn;
import java.io.IOException;
import java.util.logging.Logger;
import org.camunda.bpm.client.ExternalTaskClient;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

public class OperatorHandler {
    private final static Logger LOGGER = Logger.getLogger(OperatorHandler.class.getName());
    public static void main(String[] args) {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest") //流程运行地址
                .asyncResponseTimeout(5000) //轮询间隔
                .build();

        /**
         * 消息发送参考 https://docs.camunda.org/manual/7.5/reference/rest/message/post-message/
         * 流程入口即向消息引擎http://localhost:8080/engine-rest/message发送Post请求，带上
         * {"messageName" : "Incoming Emergency Call",
         * "businessKey" : "your businessKey"
         * }
         * 除了流程入口外的所有消息发送（无论是message event还是message task）都采用handler手动编码添加参数（为了添加businessKey），
         * 所有消息接收都采用建模器指定对应的消息名称（方便）
         */
        client.subscribe("Request paramedics intervention") .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {

                    String messageName = "Request paramedics intervention";
                    /**
                     * 多个协同的流程实例共享同一个businessKey
                     */
                    String businessKey = externalTask.getBusinessKey();
                    String messageJson = String.format("{\"messageName\":\"%s\" , \"businessKey\" : \"%s\"}", messageName,businessKey);

                    try {
                        Request.Post("http://localhost:8080/engine-rest/message")
                                .bodyString(messageJson, ContentType.APPLICATION_JSON)
                                .execute()
                                .discardContent();
                        LOGGER.info("Operator:Send Msg 'Request paramedics intervention'");
                        // 完成任务
                        externalTaskService.complete(externalTask);
                    } catch (IOException e) {
                        // 处理异常
                        externalTaskService.handleFailure(externalTask, e.getMessage(), "Failed to send message", 0, 5000);
                    }

                })
                .open();

        client.subscribe("Alert clinicians") .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {

                    String messageName = "Alert clinicians";
                    String businessKey = externalTask.getBusinessKey();
                    String messageJson = String.format("{\"messageName\":\"%s\" , \"businessKey\" : \"%s\"}", messageName,businessKey);

                    try {
                        Request.Post("http://localhost:8080/engine-rest/message")
                                .bodyString(messageJson, ContentType.APPLICATION_JSON)
                                .execute()
                                .discardContent();
                        LOGGER.info("Operator:Send Msg 'Alert clinicians'");
                        // 完成任务
                        externalTaskService.complete(externalTask);
                    } catch (IOException e) {
                        // 处理异常
                        externalTaskService.handleFailure(externalTask, e.getMessage(), "Failed to send message", 0, 5000);
                    }

                })
                .open();

        client.subscribe("Request examinations") .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {

                    String messageName = "Request examinations";
                    String businessKey = externalTask.getBusinessKey();
                    String bloodType = externalTask.getVariable("bloodType");
                    String variablesJson = String.format("{\"bloodType\":{\"value\":\"%s\", \"type\":\"String\"}}", bloodType) ;
                    String messageJson = String.format("{\"messageName\":\"%s\" , \"businessKey\" : \"%s\", \"processVariables\": %s}", messageName,businessKey,variablesJson);

                    try {
                        Request.Post("http://localhost:8080/engine-rest/message")
                                .bodyString(messageJson, ContentType.APPLICATION_JSON)
                                .execute()
                                .discardContent();
                        LOGGER.info("Operator:Send Msg 'Request examinations'");
                        // 完成任务
                        externalTaskService.complete(externalTask);
                    } catch (IOException e) {
                        // 处理异常
                        externalTaskService.handleFailure(externalTask, e.getMessage(), "Failed to send message", 0, 5000);
                    }

                })
                .open();

        client.subscribe("Request blood collection") .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {

                    String messageName = "Request blood collection";
                    String businessKey = externalTask.getBusinessKey();
                    String messageJson = String.format("{\"messageName\":\"%s\" , \"businessKey\" : \"%s\"}", messageName,businessKey);

                    try {
                        Request.Post("http://localhost:8080/engine-rest/message")
                                .bodyString(messageJson, ContentType.APPLICATION_JSON)
                                .execute()
                                .discardContent();
                        LOGGER.info("Operator:Send Msg 'Request blood collection'");
                        // 完成任务
                        externalTaskService.complete(externalTask);
                    } catch (IOException e) {
                        // 处理异常
                        externalTaskService.handleFailure(externalTask, e.getMessage(), "Failed to send message", 0, 5000);
                    }

                })
                .open();

        client.subscribe("Cancel scheduled blood collection") .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {

                    String messageName = "Cancel scheduled blood collection";
                    String businessKey = externalTask.getBusinessKey();
                    String messageJson = String.format("{\"messageName\":\"%s\" , \"businessKey\" : \"%s\"}", messageName,businessKey);

                    try {
                        Request.Post("http://localhost:8080/engine-rest/message")
                                .bodyString(messageJson, ContentType.APPLICATION_JSON)
                                .execute()
                                .discardContent();
                        LOGGER.info("Operator:Send Msg 'Cancel scheduled blood collection'");
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


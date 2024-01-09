package org.bpmn;
import java.util.logging.Logger;
import org.camunda.bpm.client.ExternalTaskClient;

public class NurseHandler {
    private final static Logger LOGGER = Logger.getLogger(NurseHandler.class.getName());
    public static void main(String[] args) {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest") //流程运行地址
                .asyncResponseTimeout(5000) //轮询间隔
                .build();

        client.subscribe("Prepare room for blood collection") .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    LOGGER.info("Nurse:Prepare room for blood collection");
                    externalTaskService.complete(externalTask);

                })
                .open();

        client.subscribe("Blood collection By Nurse") .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    LOGGER.info("Nurse:Blood collection");
                    externalTaskService.complete(externalTask);

                })
                .open();

        client.subscribe("Take care of patient") .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    LOGGER.info("Nurse:Take care of patient");
                    externalTaskService.complete(externalTask);

                })
                .open();

        client.subscribe("Blood transfusion") .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    String businessKey = externalTask.getBusinessKey();
                    String bloodType = externalTask.getVariable("bloodType");
                    LOGGER.info("Nurse:Blood transfusion");
                    LOGGER.info("The patient of BK " + businessKey + " is: " + bloodType);
                    externalTaskService.complete(externalTask);

                })
                .open();
    }
}


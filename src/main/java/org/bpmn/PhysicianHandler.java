package org.bpmn;
import java.util.logging.Logger;
import org.camunda.bpm.client.ExternalTaskClient;

public class PhysicianHandler {
    private final static Logger LOGGER = Logger.getLogger(PhysicianHandler.class.getName());
    public static void main(String[] args) {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest") //流程运行地址
                .asyncResponseTimeout(5000) //轮询间隔
                .build();

        client.subscribe("Examination") .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    LOGGER.info("Physician:Examination");
                    externalTaskService.complete(externalTask);

                })
                .open();

        client.subscribe("Schedule blood transfusion") .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    LOGGER.info("Physician:Schedule blood transfusion");
                    externalTaskService.complete(externalTask);

                })
                .open();

        client.subscribe("Take care of patient") .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    LOGGER.info("Nurse:Take care of patient");
                    externalTaskService.complete(externalTask);

                })
                .open();
    }
}


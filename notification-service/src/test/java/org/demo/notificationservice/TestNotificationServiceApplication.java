package org.demo.notificationservice;

import org.springframework.boot.SpringApplication;

public class TestNotificationServiceApplication extends AbstractIntegrationTest {

    public static void main(String[] args) {
        SpringApplication.from(NotificationServiceApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}

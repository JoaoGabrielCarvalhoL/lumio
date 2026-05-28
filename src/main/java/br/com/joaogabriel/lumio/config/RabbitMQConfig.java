package br.com.joaogabriel.lumio.config;

import io.quarkus.runtime.StartupEvent;
import io.vertx.core.Vertx;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQOptions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class RabbitMQConfig {

    private final static Logger LOG  = LoggerFactory.getLogger(RabbitMQConfig.class);

    private final String userCreateQueue;
    private final String userCreateExchange;

    private String videoUploadQueue;
    private String videoUploadExchange;

    private String userNotificationQueue;
    private String userEventsExchange;

    private final String host;
    private final int port;
    private final String user;
    private final String password;

    public RabbitMQConfig(
                          @ConfigProperty(name = "USER_CREATE_QUEUE") String userCreateQueue,
                          @ConfigProperty(name = "USER_CREATE_EXCHANGE") String userCreateExchange,
                          @ConfigProperty(name = "VIDEO_UPLOAD_QUEUE") String videoUploadQueue,
                          @ConfigProperty(name = "VIDEO_UPLOAD_EXCHANGE") String videoUploadExchange,
                          @ConfigProperty(name = "USER_NOTIFICATIONS_QUEUE") String userNotificationQueue,
                          @ConfigProperty(name = "USER_EVENTS_EXCHANGE") String userEventsExchange,
                          @ConfigProperty(name = "RABBIT_HOST") String host,
                          @ConfigProperty(name = "RABBIT_PORT") int port,
                          @ConfigProperty(name = "RABBIT_USER") String user,
                          @ConfigProperty(name = "RABBIT_PASS") String password) {
        this.userCreateQueue = userCreateQueue;
        this.userCreateExchange = userCreateExchange;
        this.videoUploadQueue = videoUploadQueue;
        this.videoUploadExchange = videoUploadExchange;
        this.userNotificationQueue = userNotificationQueue;
        this.userEventsExchange = userEventsExchange;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    void onStart(@Observes StartupEvent ev, Vertx vertx) {
        LOG.info("Starting the configuration of RabbitMQ exchanges and queues.");

        RabbitMQOptions options = new RabbitMQOptions()
                .setHost(this.host)
                .setPort(this.port)
                .setUser(this.user)
                .setPassword(this.password)
                .setVirtualHost("development");

        RabbitMQClient client = RabbitMQClient.create(vertx, options);

        client.start()
                .compose(v -> client.exchangeDeclare(userCreateExchange, "direct", true, false))
                .compose(v -> client.queueDeclare(userCreateQueue, true, false, false))
                .compose(v -> client.queueBind(userCreateQueue, userCreateExchange, "user-create-key"))

                .compose(v -> client.exchangeDeclare(videoUploadExchange, "direct", true, false))
                .compose(v -> client.queueDeclare(videoUploadQueue, true, false, false))
                .compose(v -> client.queueBind(videoUploadQueue, videoUploadExchange, "video-upload-key"))

                .compose(v -> client.exchangeDeclare(userEventsExchange, "direct", true, false))
                .compose(v -> client.queueDeclare(userNotificationQueue, true, false, false))
                .compose(v -> client.queueBind(userNotificationQueue, userEventsExchange, "user.created.success"))

                .onSuccess(v -> LOG.info("Exchanges and queues created successfully."))
                .onFailure(err -> LOG.error("Resource creation failed. Reason: {}", err.getMessage()));
    }
}

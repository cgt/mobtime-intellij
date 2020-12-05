package name.cgt.mobtimeintellij;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LearningTest {
    @Test
    public void applesauce() {

        final var mobtime = new Mobtime();
        mobtime.connect("wghweugwgoweg", message -> {
        });
        await()
          .atMost(Duration.ofSeconds(5))
          .untilAsserted(() ->
            assertEquals(List.of("{\"type\":\"timer:ownership\",\"isOwner\":true}"), mobtime.messages)
          );
    }

}

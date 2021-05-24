package tests.images.search;

import static org.assertj.core.api.Assertions.assertThat;

import api.RestWrapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pojo.Image;
import tests.ConfigurationSetupHook;

@ExtendWith(ConfigurationSetupHook.class)
public class ImagesSearchTest {

  public final RestWrapper service = new RestWrapper();

  @ParameterizedTest(name = "[{index}] size [{0}]")
  @ValueSource(strings = {"small", "med", "full", "thumb"})
  public void size(String size) {
    service.imagesSearch.search("size", size);
  }

  @ParameterizedTest(name = "[{index}] mime type [{0}]")
  @ValueSource(strings = {"jpg", "png", "gif"})
  public void mimeTypes(String mimeType) {
    service.imagesSearch.search("mime_types", mimeType)
        .statusCode(200)
        .extract()
        .jsonPath()
        .getList("url", String.class)
        .forEach(url -> assertThat(url).endsWith(mimeType));
  }

  @ParameterizedTest(name = "[{index}] includeBreeds? [{0}]")
  @ValueSource(booleans = {true, false})
  public void includeBreeds(boolean includeBreeds) {
    service.imagesSearch.search("include_breeds", includeBreeds)
        .statusCode(200)
        .extract()
        .jsonPath()
        .getList("$", Image.class)
        .forEach(image -> assertThat(image.getBreeds() != null).isEqualTo(includeBreeds));
  }

  @ParameterizedTest(name = "[{index}] format [{0}]")
  @ValueSource(strings = {"json", "src"})
  public void format(String format) {
    service.imagesSearch.search(format)
        .statusCode(200)
        .contentType(getContentTypeByFormat(format));
  }

  private String getContentTypeByFormat(String format) {
    switch (format) {
      case "json":
        return "application/json; charset=utf-8";
      case "src":
        return "image/jpeg";
      default:
        throw new RuntimeException(
            String.format("Not able to find proper Content Type for [%s] format", format));
    }
  }
}

package mapping;

import java.io.File;
import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.Test;

import play.db.jpa.Model;
import play.modules.elasticsearch.annotations.ElasticSearchable;
import play.modules.elasticsearch.mapping.ModelMapper;

/**
 * Tests for simple mappings (primitives)
 */
public class FileAttachmentMappingTest extends MappingTest {

    @SuppressWarnings("serial")
    @ElasticSearchable
    public static class TestModel extends Model {
        public File file;

    }

    @Test
    public void testSimpleMapping() throws IOException {
        ModelMapper<TestModel> mapper = getMapper(TestModel.class);
        assertNotNull(mapper);

        // Get generated mapping
        XContentBuilder generatedMapping = mappingFor(mapper);

        // Build mapping locally for verification
        XContentBuilder mapping = builder();
        mapping.startObject();
        mapping.startObject(mapper.getTypeName());
        mapping.startObject("properties");

        mapping.startObject("file");
        mapping.field("type", "attachment");
        mapping.endObject();

        // Play model id
        mapping.startObject("id");
        mapping.field("type", "long");
        mapping.endObject();

        mapping.endObject();
        mapping.endObject();
        mapping.endObject();

        assertEquals(mapping.string(), generatedMapping.string());
    }
}

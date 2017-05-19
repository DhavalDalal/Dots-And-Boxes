package dots_and_boxes.step_definitions;

import cucumber.deps.com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import dots_and_boxes.Dot;


public class DotConverter extends AbstractSingleValueConverter {
    @Override
    public boolean canConvert(Class type) {
        return type.equals(Dot.class);
    }

    @Override
    public Object fromString(String value) {
        String [] coordinates = value.replace("{","").replace("}", "").split(",");
        return new Dot(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
    }
}

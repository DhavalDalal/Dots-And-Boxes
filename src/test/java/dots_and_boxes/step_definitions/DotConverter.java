package dots_and_boxes.step_definitions;

import cucumber.api.Transformer;
import dots_and_boxes.Dot;

public class DotConverter extends Transformer<Dot> {
    @Override
    public Dot transform(String value) {
        String [] coordinates = value.replace("{","").replace("}", "").split(",");
        return new Dot(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
    }
}

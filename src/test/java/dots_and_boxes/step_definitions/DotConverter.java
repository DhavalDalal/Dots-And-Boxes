package dots_and_boxes.step_definitions;

import cucumber.api.Transformer;
import dots_and_boxes.Dot;

public class DotConverter extends Transformer<Dot> {
    @Override
    public Dot transform(String value) {
        final String[] coordinates = value.replace("{", "").replace("}", "").split(",");
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);
        return new Dot(x, y);
    }
}

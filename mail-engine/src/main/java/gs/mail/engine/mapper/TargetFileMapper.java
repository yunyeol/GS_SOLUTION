package gs.mail.engine.mapper;

import gs.mail.engine.dto.Target;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class TargetFileMapper implements FieldSetMapper<Target> {
    @Override
    public Target mapFieldSet(FieldSet fieldSet) throws BindException {
        Target target = new Target();
        target.setMbrAddress(fieldSet.readString("address"));
        target.setMbrName(fieldSet.readString("name"));
        target.setMap1(fieldSet.readString("map1"));
        target.setMap2(fieldSet.readString("map2"));
        target.setMap3(fieldSet.readString("map3"));
        target.setMap4(fieldSet.readString("map4"));
        target.setMap5(fieldSet.readString("map5"));
        target.setMap6(fieldSet.readString("map6"));
        target.setMap7(fieldSet.readString("map7"));
        target.setMap8(fieldSet.readString("map8"));
        target.setMap9(fieldSet.readString("map9"));
        target.setMap10(fieldSet.readString("map10"));
        return target;
    }
}

package cn.hperfect.apikit.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author hperfect
 * @date 2023/5/8 13:42
 */
@AllArgsConstructor
@Getter
public class GetterSetter {
    private final String label;
    private final Supplier<String> getter;
    private final Consumer<String> setter;

}

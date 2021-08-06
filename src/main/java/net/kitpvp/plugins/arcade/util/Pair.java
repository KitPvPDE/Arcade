package net.kitpvp.plugins.arcade.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Pair<F, S> {

    private final F first;
    private final S second;

}

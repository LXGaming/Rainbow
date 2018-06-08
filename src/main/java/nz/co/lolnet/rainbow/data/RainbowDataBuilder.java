/*
 * Copyright 2018 lolnet.co.nz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nz.co.lolnet.rainbow.data;

import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class RainbowDataBuilder extends AbstractDataBuilder<RainbowData> implements DataManipulatorBuilder<RainbowData, RainbowImmutableData> {
    
    public RainbowDataBuilder() {
        super(RainbowData.class, 1);
    }
    
    public static RainbowDataBuilder builder() {
        return new RainbowDataBuilder();
    }
    
    @Override
    public RainbowData create() {
        return new RainbowData();
    }
    
    @Override
    public Optional<RainbowData> createFrom(DataHolder dataHolder) {
        return create().fill(dataHolder);
    }
    
    @Override
    protected Optional<RainbowData> buildContent(DataView container) throws InvalidDataException {
        return create().from(container.copy());
    }
}
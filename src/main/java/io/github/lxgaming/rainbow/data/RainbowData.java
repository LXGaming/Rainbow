/*
 * Copyright 2018 Alex Thomson
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

package io.github.lxgaming.rainbow.data;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.MutableBoundedValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.TypeTokens;

import java.util.Optional;
import java.util.UUID;

public class RainbowData extends AbstractData<RainbowData, RainbowImmutableData> {
    
    public static final Key<Value<UUID>> UNIQUE_ID_KEY = Key.builder()
            .type(TypeTokens.UUID_VALUE_TOKEN)
            .id("unique_id")
            .name("Unique Id")
            .query(DataQuery.of("UniqueId"))
            .build();
    
    public static final Key<MutableBoundedValue<Float>> HUE_KEY = Key.builder()
            .type(TypeTokens.FLOAT_VALUE_TOKEN)
            .id("hue")
            .name("Hue")
            .query(DataQuery.of("Hue"))
            .build();
    
    private UUID uniqueId;
    private float hue;
    
    RainbowData() {
        this(null, 0.0F);
    }
    
    public RainbowData(UUID uniqueId, float hue) {
        this.uniqueId = uniqueId;
        this.hue = hue;
        registerGettersAndSetters();
    }
    
    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(UNIQUE_ID_KEY, this::getUniqueId);
        registerFieldSetter(UNIQUE_ID_KEY, this::setUniqueId);
        registerKeyValue(UNIQUE_ID_KEY, this::uniqueId);
        
        registerFieldGetter(HUE_KEY, this::getHue);
        registerFieldSetter(HUE_KEY, this::setHue);
        registerKeyValue(HUE_KEY, this::hue);
    }
    
    @Override
    public Optional<RainbowData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<UUID> uniqueId = dataHolder.get(UNIQUE_ID_KEY);
        Optional<Float> hue = dataHolder.get(HUE_KEY);
        if (uniqueId.isPresent() && hue.isPresent()) {
            RainbowData rainbowData = this.copy();
            rainbowData.setUniqueId(uniqueId.get());
            rainbowData.setHue(hue.get());
            rainbowData = overlap.merge(this, rainbowData);
            setUniqueId(rainbowData.getUniqueId());
            setHue(rainbowData.getHue());
            return Optional.of(this);
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<RainbowData> from(DataContainer container) {
        Optional<UUID> uniqueId = container.getObject(UNIQUE_ID_KEY.getQuery(), UUID.class);
        Optional<Float> hue = container.getFloat(HUE_KEY.getQuery());
        if (uniqueId.isPresent() && hue.isPresent()) {
            setUniqueId(uniqueId.get());
            setHue(hue.get());
            return Optional.of(this);
        }
        
        return Optional.empty();
    }
    
    @Override
    public RainbowData copy() {
        return new RainbowData(getUniqueId(), getHue());
    }
    
    @Override
    public RainbowImmutableData asImmutable() {
        return new RainbowImmutableData(getUniqueId(), getHue());
    }
    
    @Override
    public int getContentVersion() {
        return 1;
    }
    
    @Override
    public DataContainer toContainer() {
        DataContainer dataContainer = super.toContainer();
        dataContainer.set(UNIQUE_ID_KEY, getUniqueId());
        dataContainer.set(HUE_KEY, getHue());
        return dataContainer;
    }
    
    public UUID getUniqueId() {
        return uniqueId;
    }
    
    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    private Value<UUID> uniqueId() {
        return Sponge.getRegistry().getValueFactory().createValue(UNIQUE_ID_KEY, getUniqueId());
    }
    
    public float getHue() {
        return hue;
    }
    
    public void setHue(float hue) {
        this.hue = hue;
    }
    
    private MutableBoundedValue<Float> hue() {
        return Sponge.getRegistry().getValueFactory().createBoundedValueBuilder(HUE_KEY)
                .actualValue(getHue())
                .defaultValue(0.0F)
                .maximum(360.0F)
                .minimum(0.0F)
                .build();
    }
}
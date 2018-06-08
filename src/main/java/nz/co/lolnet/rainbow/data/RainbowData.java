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

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
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
    
    private UUID uniqueId;
    
    protected RainbowData() {
        this(null);
    }
    
    public RainbowData(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    @Override
    public Optional<RainbowData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<UUID> uniqueId = dataHolder.get(UNIQUE_ID_KEY);
        if (uniqueId.isPresent()) {
            RainbowData rainbowData = this.copy();
            rainbowData.setUniqueId(uniqueId.get());
            rainbowData = overlap.merge(this, rainbowData);
            setUniqueId(rainbowData.getUniqueId());
            return Optional.of(this);
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<RainbowData> from(DataContainer container) {
        Optional<UUID> uniqueId = container.getObject(UNIQUE_ID_KEY.getQuery(), UUID.class);
        if (uniqueId.isPresent()) {
            setUniqueId(uniqueId.get());
            return Optional.of(this);
        }
        
        return Optional.empty();
    }
    
    @Override
    public RainbowData copy() {
        return new RainbowData(getUniqueId());
    }
    
    @Override
    public RainbowImmutableData asImmutable() {
        return new RainbowImmutableData(getUniqueId());
    }
    
    @Override
    public int getContentVersion() {
        return 1;
    }
    
    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(UNIQUE_ID_KEY, this::getUniqueId);
        registerFieldSetter(UNIQUE_ID_KEY, this::setUniqueId);
        registerKeyValue(UNIQUE_ID_KEY, this::uniqueId);
    }
    
    @Override
    public DataContainer toContainer() {
        DataContainer dataContainer = super.toContainer();
        dataContainer.set(UNIQUE_ID_KEY, getUniqueId());
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
}
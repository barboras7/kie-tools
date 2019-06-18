/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.dmn.client.editors.included.imports.persistence;

import java.util.List;
import java.util.Objects;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.kie.workbench.common.dmn.api.definition.v1_1.Import;
import org.kie.workbench.common.dmn.api.property.dmn.Name;
import org.kie.workbench.common.dmn.client.editors.common.messages.FlashMessage;
import org.kie.workbench.common.dmn.client.editors.common.persistence.RecordEngine;
import org.kie.workbench.common.dmn.client.editors.included.BaseIncludedModelActiveRecord;
import org.kie.workbench.common.dmn.client.editors.included.imports.ImportFactory;
import org.kie.workbench.common.dmn.client.editors.included.imports.IncludedModelsIndex;
import org.kie.workbench.common.dmn.client.editors.included.imports.IncludedModelsPageStateProviderImpl;
import org.kie.workbench.common.dmn.client.editors.included.imports.messages.IncludedModelErrorMessageFactory;

import static java.util.Collections.singletonList;
import static org.kie.workbench.common.stunner.core.util.StringUtils.isEmpty;

public class ImportRecordEngine implements RecordEngine<BaseIncludedModelActiveRecord> {

    private final IncludedModelsPageStateProviderImpl stateProvider;

    private final IncludedModelsIndex includedModelsIndex;

    private final IncludedModelErrorMessageFactory messageFactory;

    private final ImportFactory importFactory;

    private final Event<FlashMessage> flashMessageEvent;

    private final DefinitionsHandler definitionsHandler;

    private final ItemDefinitionHandler itemDefinitionHandler;

    private final DRGElementHandler drgElementHandler;

    @Inject
    public ImportRecordEngine(final IncludedModelsPageStateProviderImpl stateProvider,
                              final IncludedModelsIndex includedModelsIndex,
                              final IncludedModelErrorMessageFactory messageFactory,
                              final ImportFactory importFactory,
                              final Event<FlashMessage> flashMessageEvent,
                              final DefinitionsHandler definitionsHandler,
                              final ItemDefinitionHandler itemDefinitionHandler,
                              final DRGElementHandler drgElementHandler) {
        this.stateProvider = stateProvider;
        this.includedModelsIndex = includedModelsIndex;
        this.messageFactory = messageFactory;
        this.importFactory = importFactory;
        this.flashMessageEvent = flashMessageEvent;
        this.definitionsHandler = definitionsHandler;
        this.itemDefinitionHandler = itemDefinitionHandler;
        this.drgElementHandler = drgElementHandler;
    }

    @Override
    public List<BaseIncludedModelActiveRecord> update(final BaseIncludedModelActiveRecord record) {
        if (!record.isValid()) {
            throw new UnsupportedOperationException("An invalid Included Model cannot be updated.");
        }

        final Import anImport = getImport(record);
        final String oldModelName = anImport.getName().getValue();

        itemDefinitionHandler.update(oldModelName, record.getName());
        drgElementHandler.update(oldModelName, record.getName());
        anImport.setName(new Name(record.getName()));

        return singletonList(record);
    }

    @Override
    public List<BaseIncludedModelActiveRecord> destroy(final BaseIncludedModelActiveRecord record) {
        definitionsHandler.destroy(record);
        itemDefinitionHandler.destroy(record.getName());
        drgElementHandler.destroy(record.getName());
        stateProvider.getImports().remove(getImport(record));
        return singletonList(record);
    }

    @Override
    public List<BaseIncludedModelActiveRecord> create(final BaseIncludedModelActiveRecord record) {
        definitionsHandler.create(record);
        stateProvider.getImports().add(importFactory.makeImport(record));
        return singletonList(record);
    }

    @Override
    public boolean isValid(final BaseIncludedModelActiveRecord record) {

        if (!isUniqueName(record)) {
            flashMessageEvent.fire(messageFactory.getNameIsNotUniqueFlashMessage(record));
            return false;
        }

        if (isBlankName(record)) {
            flashMessageEvent.fire(messageFactory.getNameIsBlankFlashMessage(record));
            return false;
        }

        return true;
    }

    private boolean isUniqueName(final BaseIncludedModelActiveRecord record) {
        return stateProvider
                .getImports()
                .stream()
                .noneMatch(anImport -> !sameImport(record, anImport) && sameName(record, anImport));
    }

    private boolean isBlankName(final BaseIncludedModelActiveRecord record) {
        return isEmpty(record.getName());
    }

    private boolean sameName(final BaseIncludedModelActiveRecord record, final Import anImport) {
        return Objects.equals(record.getName(), anImport.getName().getValue());
    }

    private boolean sameImport(final BaseIncludedModelActiveRecord record, final Import anImport) {
        final Import recordImport = getImport(record);
        return Objects.equals(recordImport, anImport);
    }

    private Import getImport(final BaseIncludedModelActiveRecord record) {
        return includedModelsIndex.getImport(record);
    }
}

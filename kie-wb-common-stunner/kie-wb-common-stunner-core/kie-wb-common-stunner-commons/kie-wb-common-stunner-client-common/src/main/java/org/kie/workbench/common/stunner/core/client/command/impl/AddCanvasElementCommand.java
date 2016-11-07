/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.core.client.command.impl;

import org.kie.workbench.common.stunner.core.client.canvas.AbstractCanvasHandler;
import org.kie.workbench.common.stunner.core.client.command.AbstractCanvasGraphCommand;
import org.kie.workbench.common.stunner.core.client.command.CanvasViolation;
import org.kie.workbench.common.stunner.core.client.shape.MutationContext;
import org.kie.workbench.common.stunner.core.client.shape.factory.ShapeFactory;
import org.kie.workbench.common.stunner.core.command.CommandResult;
import org.kie.workbench.common.stunner.core.graph.Element;

public abstract class AddCanvasElementCommand<E extends Element> extends AbstractCanvasGraphCommand {

    protected final E candidate;
    protected final ShapeFactory factory;

    public AddCanvasElementCommand( final E candidate, final ShapeFactory factory ) {
        this.candidate = candidate;
        this.factory = factory;
    }

    @Override
    public CommandResult<CanvasViolation> doCanvasExecute( final AbstractCanvasHandler context ) {
        doRegister( context );
        doMutate( context );
        return buildResult();
    }

    protected void doRegister( final AbstractCanvasHandler context ) {
        context.register( factory, candidate );
    }

    protected void doMutate( final AbstractCanvasHandler context ) {
        context.applyElementMutation( candidate, MutationContext.STATIC );
    }

}

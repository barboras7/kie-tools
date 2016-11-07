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
package org.kie.workbench.common.stunner.core.graph.command.impl;

import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.kie.workbench.common.stunner.core.command.CommandResult;
import org.kie.workbench.common.stunner.core.graph.Edge;
import org.kie.workbench.common.stunner.core.graph.Graph;
import org.kie.workbench.common.stunner.core.graph.Node;
import org.kie.workbench.common.stunner.core.graph.command.GraphCommandExecutionContext;
import org.kie.workbench.common.stunner.core.graph.command.GraphCommandResultBuilder;
import org.kie.workbench.common.stunner.core.rule.RuleViolation;

import java.util.Iterator;

/**
 * A Command to clear all elements in a graph
 * <p>
 * TODO: Undo.
 */
@Portable
public final class ClearGraphCommand extends AbstractGraphCommand {

    private final String rootUUID;

    public ClearGraphCommand( @MapsTo( "rootUUID" ) String rootUUID ) {
        this.rootUUID = rootUUID;
    }

    @Override
    public CommandResult<RuleViolation> allow( final GraphCommandExecutionContext context ) {
        return check( context );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public CommandResult<RuleViolation> execute( final GraphCommandExecutionContext context ) {
        final CommandResult<RuleViolation> results = check( context );
        if ( !results.getType().equals( CommandResult.Type.ERROR ) ) {
            final Graph<?, Node> graph = getGraph( context );
            if ( hasRootUUID() ) {
                Iterator<Node> nodes = graph.nodes().iterator();
                if ( null != nodes ) {
                    nodes.forEachRemaining( node -> {
                        if ( !node.getUUID().equals( rootUUID ) ) {
                            getMutableIndex( context ).removeNode( node );
                            nodes.remove();
                        } else {
                            // Clear outgoing edges for canvas root element.
                            node.getOutEdges().stream().forEach( edge -> getMutableIndex( context ).removeEdge( ( Edge ) edge ) );
                            node.getOutEdges().clear();
                        }
                    } );
                }
            } else {
                graph.clear();
                getMutableIndex( context ).clear();
            }
        }
        return results;
    }

    protected CommandResult<RuleViolation> doCheck( final GraphCommandExecutionContext context ) {
        if ( hasRootUUID() ) {
            checkNodeNotNull( context, rootUUID );
        }
        return GraphCommandResultBuilder.SUCCESS;
    }

    @Override
    public CommandResult<RuleViolation> undo( GraphCommandExecutionContext context ) {
        throw new UnsupportedOperationException( "Clear graph command undo is still not supported. " );
    }

    private boolean hasRootUUID() {
        return null != this.rootUUID && rootUUID.trim().length() > 0;
    }

    @Override
    public String toString() {
        return "ClearGraphCommand [rootUUID=" + rootUUID + "]";
    }
}

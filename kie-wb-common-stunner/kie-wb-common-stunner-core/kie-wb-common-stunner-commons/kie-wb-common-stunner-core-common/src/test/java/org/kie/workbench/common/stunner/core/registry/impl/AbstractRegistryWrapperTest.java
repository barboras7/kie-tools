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

package org.kie.workbench.common.stunner.core.registry.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.stunner.core.registry.Registry;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class AbstractRegistryWrapperTest {

    @Mock
    Registry<Object> registry;

    private AbstractRegistryWrapper tested;
    private String s1 = "s1";
    private String s2 = "s2";

    @Before
    public void setup() throws Exception {
        when( registry.contains( anyObject() ) ).thenReturn( false );
        when( registry.contains( eq( s1 ) ) ).thenReturn( true );
        when( registry.contains( eq( s2 ) ) ).thenReturn( true );
        when( registry.isEmpty() ).thenReturn( false );
        tested = new AbstractRegistryWrapper<Object, Registry<Object>>( registry ) {
        };
    }

    @Test
    @SuppressWarnings( "unchecked" )
    public void testContains() {
        assertTrue( tested.contains( s1 ) );
        assertTrue( tested.contains( s2 ) );
        assertFalse( tested.contains( "" ) );
    }

    @Test
    public void testEmpty() {
        when( registry.isEmpty() ).thenReturn( true );
        boolean empty = tested.isEmpty();
        assertTrue( empty );
    }

    @Test
    public void testNotEmpty() {
        boolean empty = registry.isEmpty();
        assertFalse( empty );
    }

}

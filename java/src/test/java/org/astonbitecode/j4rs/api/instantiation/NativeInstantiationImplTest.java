/*
 * Copyright 2018 astonbitecode
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
package org.astonbitecode.j4rs.api.instantiation;

import org.astonbitecode.j4rs.api.dtos.GeneratedArg;
import org.astonbitecode.j4rs.api.dtos.InvocationArg;
import org.astonbitecode.j4rs.api.invocation.JsonInvocationImpl;
import org.astonbitecode.j4rs.utils.Dummy;
import org.junit.Test;

public class NativeInstantiationImplTest {

    @Test
    public void constructorMatches() throws Exception {
        String className = Dummy.class.getName();

        GeneratedArg[] generatedArgs = {new GeneratedArg(Integer.class, new Integer(11))};
        NativeInstantiationImpl.CreatedInstance createdInstance = NativeInstantiationImpl.createInstance(className, generatedArgs);
        assert (createdInstance.getObject() instanceof Dummy);

        GeneratedArg[] noGeneratedArgs = {};
        NativeInstantiationImpl.CreatedInstance createdInstanceNoArgs = NativeInstantiationImpl.createInstance(className, noGeneratedArgs);
        assert (createdInstanceNoArgs.getObject() instanceof Dummy);
    }

    @Test(expected = Exception.class)
    public void noConstructorFound() throws Exception {
        String className = Dummy.class.getName();

        GeneratedArg[] generatedArgs = {new GeneratedArg(Long.class, new Long(11))};
        NativeInstantiationImpl.createInstance(className, generatedArgs);
    }

    @Test
    public void generateArgObjectsFromJava() throws Exception {
        InvocationArg arg = new InvocationArg(Dummy.class.getName(), new JsonInvocationImpl(new Dummy(), Dummy.class));
        InvocationArg[] args = {arg};

        GeneratedArg[] generated = NativeInstantiationImpl.generateArgObjects(args);
        assert (generated.length == 1);
        assert (generated[0].getClazz().equals(Dummy.class));
    }

    @Test
    public void generateArgObjectsFromRust() throws Exception {
        String json = "{\"i\":0}";
        InvocationArg arg = new InvocationArg(Dummy.class.getName(), json);
        InvocationArg[] args = {arg};

        GeneratedArg[] generated = NativeInstantiationImpl.generateArgObjects(args);
        assert (generated.length == 1);
        assert (generated[0].getClazz().equals(Dummy.class));
    }

    @Test
    public void createJavaArraySuccess() throws Exception {
        String className = Integer.class.getName();
        GeneratedArg[] generatedArgs = {new GeneratedArg(Integer.class, new Integer(11))};
        NativeInstantiationImpl.CreatedInstance createdInstance = NativeInstantiationImpl.doCreateJavaArray(className, generatedArgs);
        assert (createdInstance.getClazz().getName().equals("[Ljava.lang.Integer;"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createJavaArrayFailure() throws Exception {
        String className = Integer.class.getName();
        GeneratedArg[] generatedArgs = {
                new GeneratedArg(Integer.class, new Integer(11)),
                new GeneratedArg(String.class, "this is a string")
        };
        NativeInstantiationImpl.doCreateJavaArray(className, generatedArgs);
    }
}

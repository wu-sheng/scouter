// Generated by delombok at Sun Feb 26 12:31:38 KST 2017
package scouter.bytebuddy.implementation.bind.annotation;

import scouter.bytebuddy.description.annotation.AnnotationDescription;
import scouter.bytebuddy.description.method.MethodDescription;
import scouter.bytebuddy.description.method.MethodList;
import scouter.bytebuddy.description.method.ParameterDescription;
import scouter.bytebuddy.description.type.TypeDescription;
import scouter.bytebuddy.dynamic.TargetType;
import scouter.bytebuddy.implementation.Implementation;
import scouter.bytebuddy.implementation.auxiliary.TypeProxy;
import scouter.bytebuddy.implementation.bind.MethodDelegationBinder;
import scouter.bytebuddy.implementation.bytecode.assign.Assigner;
import scouter.bytebuddy.implementation.MethodDelegation;
import scouter.bytebuddy.matcher.ElementMatchers;

import java.lang.annotation.*;

/**
 * Parameters that are annotated with this annotation are assigned an instance of an auxiliary proxy type that allows calling
 * any default method of an interface of the instrumented type where the parameter type must be an interface that is
 * directly implemented by the instrumented type. The generated proxy will directly implement the parameter's
 * interface. If the interface of the annotation is not implemented by the instrumented type, the method with this
 * parameter is not considered as a binding target.
 *
 * @see MethodDelegation
 * @see TargetMethodAnnotationDrivenBinder
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Default {
    /**
     * Determines if the generated proxy should be {@link java.io.Serializable}. If the annotated type
     * already is serializable, such an explicit specification is not required.
     *
     * @return {@code true} if the generated proxy should be {@link java.io.Serializable}.
     */
    boolean serializableProxy() default false;

    /**
     * Determines the type that is implemented by the proxy. When this value is set to its default value
     * {@code void}, the proxy is created as an instance of the parameter's type. It is <b>not</b> possible to
     * set the value of this property to {@link TargetType} as a interface cannot implement itself.
     *
     * @return The type of the proxy or an indicator type, i.e. {@code void}.
     */
    Class<?> proxyType() default void.class;


    /**
     * A binder for the {@link Default} annotation.
     */
    enum Binder implements TargetMethodAnnotationDrivenBinder.ParameterBinder<Default> {
        /**
         * The singleton instance.
         */
        INSTANCE;
        /**
         * A method reference to the serializable proxy property.
         */
        private static final MethodDescription.InDefinedShape SERIALIZABLE_PROXY;
        /**
         * A method reference to the proxy type property.
         */
        private static final MethodDescription.InDefinedShape PROXY_TYPE;

        /*
         * Extracts method references of the default annotation.
         */
        static {
            MethodList<MethodDescription.InDefinedShape> annotationProperties = new TypeDescription.ForLoadedType(Default.class).getDeclaredMethods();
            SERIALIZABLE_PROXY = annotationProperties.filter(ElementMatchers.named("serializableProxy")).getOnly();
            PROXY_TYPE = annotationProperties.filter(ElementMatchers.named("proxyType")).getOnly();
        }

        @Override
        public Class<Default> getHandledType() {
            return Default.class;
        }

        @Override
        public MethodDelegationBinder.ParameterBinding<?> bind(AnnotationDescription.Loadable<Default> annotation, MethodDescription source, ParameterDescription target, Implementation.Target implementationTarget, Assigner assigner, Assigner.Typing typing) {
            TypeDescription proxyType = TypeLocator.ForType.of(annotation.getValue(PROXY_TYPE).resolve(TypeDescription.class)).resolve(target.getType());
            if (!proxyType.isInterface()) {
                throw new IllegalStateException(target + " uses the @Default annotation on an invalid type");
            }
            if (source.isStatic() || !implementationTarget.getInstrumentedType().getInterfaces().asErasures().contains(proxyType)) {
                return MethodDelegationBinder.ParameterBinding.Illegal.INSTANCE;
            } else {
                return new MethodDelegationBinder.ParameterBinding.Anonymous(new TypeProxy.ForDefaultMethod(proxyType, implementationTarget, annotation.getValue(SERIALIZABLE_PROXY).resolve(Boolean.class)));
            }
        }

        /**
         * Locates the type which should be the base type of the created proxy.
         */
        protected interface TypeLocator {
            /**
             * Resolves the target type.
             *
             * @param parameterType The type of the target parameter.
             * @return The proxy type.
             */
            TypeDescription resolve(TypeDescription.Generic parameterType);

            /**
             * A type locator that yields the target parameter's type.
             */
            enum ForParameterType implements TypeLocator {
                /**
                 * The singleton instance.
                 */
                INSTANCE;

                @Override
                public TypeDescription resolve(TypeDescription.Generic parameterType) {
                    return parameterType.asErasure();
                }
            }

            /**
             * A type locator that returns a given type.
             */
            class ForType implements TypeLocator {
                /**
                 * The type to be returned upon resolution.
                 */
                private final TypeDescription typeDescription;

                /**
                 * Creates a new type locator for a given type.
                 *
                 * @param typeDescription The type to be returned upon resolution.
                 */
                protected ForType(TypeDescription typeDescription) {
                    this.typeDescription = typeDescription;
                }

                /**
                 * Resolves a type locator based upon an annotation value.
                 *
                 * @param typeDescription The annotation's value.
                 * @return The appropriate type locator.
                 */
                protected static TypeLocator of(TypeDescription typeDescription) {
                    if (typeDescription.represents(void.class)) {
                        return ForParameterType.INSTANCE;
                    } else if (!typeDescription.isInterface()) {
                        throw new IllegalStateException("Cannot assign proxy to " + typeDescription);
                    } else {
                        return new ForType(typeDescription);
                    }
                }

                @Override
                public TypeDescription resolve(TypeDescription.Generic parameterType) {
                    if (!typeDescription.isAssignableTo(parameterType.asErasure())) {
                        throw new IllegalStateException("Impossible to assign " + typeDescription + " to parameter of type " + parameterType);
                    }
                    return typeDescription;
                }

                @java.lang.Override
                @java.lang.SuppressWarnings("all")
                @javax.annotation.Generated("lombok")
                public boolean equals(final java.lang.Object o) {
                    if (o == this) return true;
                    if (!(o instanceof Default.Binder.TypeLocator.ForType)) return false;
                    final Default.Binder.TypeLocator.ForType other = (Default.Binder.TypeLocator.ForType) o;
                    if (!other.canEqual((java.lang.Object) this)) return false;
                    final java.lang.Object this$typeDescription = this.typeDescription;
                    final java.lang.Object other$typeDescription = other.typeDescription;
                    if (this$typeDescription == null ? other$typeDescription != null : !this$typeDescription.equals(other$typeDescription)) return false;
                    return true;
                }

                @java.lang.SuppressWarnings("all")
                @javax.annotation.Generated("lombok")
                protected boolean canEqual(final java.lang.Object other) {
                    return other instanceof Default.Binder.TypeLocator.ForType;
                }

                @java.lang.Override
                @java.lang.SuppressWarnings("all")
                @javax.annotation.Generated("lombok")
                public int hashCode() {
                    final int PRIME = 59;
                    int result = 1;
                    final java.lang.Object $typeDescription = this.typeDescription;
                    result = result * PRIME + ($typeDescription == null ? 43 : $typeDescription.hashCode());
                    return result;
                }
            }
        }
    }
}
package dominio.model;


import dominio.functions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.lang.Object;


/**
 * Patrón factoría para tener una instancia de una función aplicada en una celda
 */
public class GetFunctionFactory {

    /**
     * Representa todas las funciones que se pueden aplicar en la hoja de cálculo con sus clases
     */
    private static final Map<String, Class<? extends Function>> instances = new HashMap<>();

    static {
        GetFunctionFactory.register("absolute", Absolute.class);
        GetFunctionFactory.register("pearson", PearsonCorrelation.class);
        GetFunctionFactory.register("truncate", Truncate.class);
        GetFunctionFactory.register("lengthText", LengthText.class);
        GetFunctionFactory.register("mean", Mean.class);
        GetFunctionFactory.register("standard", StandardDeviation.class);
        GetFunctionFactory.register("increment", Increment.class);
        GetFunctionFactory.register("variance", Variance.class);
        GetFunctionFactory.register("replaceText", ReplaceText.class);
        GetFunctionFactory.register("median", Median.class);
        GetFunctionFactory.register("dayOfWeek", DayOfWeek.class);
        GetFunctionFactory.register("covariance", Covariance.class);
        GetFunctionFactory.register("elementExtraction", ElementExtraction.class);
        GetFunctionFactory.register("floor", Floor.class);
        GetFunctionFactory.register("identity", Identity.class);
    }

    /**
     * Registra una nueva función en la factoría
     * @param functionName String con la función a registrar
     * @param instance instancia de la clase para insertar a la factoría
     */
    public static void register(String functionName, Class<? extends Function> instance) {
        if (functionName != null && instance != null) {
            instances.put(functionName, instance);
        }
    }

    /**
     * Retorna una instancia de una función determinada que esté dentro de la factoría
     * @param functionName String con la función de la cual se quiere obtener una instancia
     * @return Function con la instancia de la clase que se quiere proporcionar
     * @throws NoSuchMethodException si no se encuentra el método
     * @throws InvocationTargetException si se produce un error en la invocación
     * @throws IllegalAccessException si no se puede acceder al método
     * @throws InstantiationException si no se puede instanciar la clase
     */
    public static Function getInstance(String functionName) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> cFunction = instances.get(functionName);
        Constructor<?> functionConstructor = cFunction.getDeclaredConstructor();
        return (Function)functionConstructor.newInstance(new Object[] { });
    }

}

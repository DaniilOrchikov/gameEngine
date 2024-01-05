package util

object Injector {
    private val services = mutableMapOf<Class<*>, Any>()

    fun <T : Any> addService(serviceClass: Class<T>, instance: T) {
        services[serviceClass] = instance
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getService(serviceClass: Class<T>): T {
        return services[serviceClass] as T
            ?: throw IllegalArgumentException("No service found for class: ${serviceClass.simpleName}")
    }
}
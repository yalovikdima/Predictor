import com.netcosports.rooibos.Swift

@Swift
enum class Scene {
    REFRESHING,
    EMPTY_ERROR,
    EMPTY_LOADING,
    DATA;

    val isRefreshing: Boolean get() = this == REFRESHING
    val isError: Boolean get() = this == EMPTY_ERROR
    val isEmptyLoading: Boolean get() = this == EMPTY_LOADING

}

fun Scene.toLoad(): Scene {
    return when {
        isError -> Scene.EMPTY_LOADING
        this == Scene.DATA -> Scene.REFRESHING
        else -> this
    }
}
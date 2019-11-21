package tech.bitcube.template.data.network.response


interface BaseDto<Entity> {
    fun toEntity(): Entity
}
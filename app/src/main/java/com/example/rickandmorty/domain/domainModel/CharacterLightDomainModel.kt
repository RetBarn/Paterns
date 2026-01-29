package com.example.rickandmorty.domain.domainModel

data class CharacterLightDomainModel(
    val id: Long,
    val name: String,
    val status: CharactersStatus,
    val species: String,
    val imageUrl: String,
    val isFavorite: Boolean = false,
    val accessCount: Int = 0
)

fun CharacterLightDomainModel.toAllCharacterDomainModel(): AllCharacterDomainModel {
    return AllCharacterDomainModel(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        type = "",
        gender = CharactersGender.UNKNOWN,
        location = CharacterLocationDomainModel(-1, ""),
        origin = CharacterLocationDomainModel(-1, ""),
        image = this.imageUrl,
        episodes = emptyList(),
        isFavorite = this.isFavorite
    )
}

fun List<CharacterLightDomainModel>.toAllCharacterDomainModelList(): List<AllCharacterDomainModel> {
    return this.map { it.toAllCharacterDomainModel() }
}
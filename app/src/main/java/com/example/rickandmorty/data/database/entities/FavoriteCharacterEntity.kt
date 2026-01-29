package com.example.rickandmorty.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rickandmorty.domain.domainModel.AllCharacterDomainModel
import com.example.rickandmorty.domain.domainModel.CharacterLocationDomainModel
import com.example.rickandmorty.domain.domainModel.CharactersGender
import com.example.rickandmorty.domain.domainModel.CharactersStatus

@Entity("favoriteCharacter")
data class FavoriteCharacterEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("status")
    val status: CharactersStatus,

    @ColumnInfo("species")
    val species: String,

    @ColumnInfo("type")
    val type: String,

    @ColumnInfo("gender")
    val gender: CharactersGender,

    @Embedded(prefix = "location_")
    val location: LocationEmbeddable,

    @Embedded(prefix = "origin_")
    val origin: LocationEmbeddable,

    @ColumnInfo("image")
    val image: String,

    @ColumnInfo("episodes")
    val episodes: String,

    @ColumnInfo("isFavorite")
    val isFavorite: Boolean = false
)

fun FavoriteCharacterEntity.toDomain(): AllCharacterDomainModel {
    return AllCharacterDomainModel(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        type = this.type,
        gender = this.gender,
        location = CharacterLocationDomainModel(
            id = this.location.id,
            name = this.location.name
        ),
        origin = CharacterLocationDomainModel(
            id = this.origin.id,
            name = this.origin.name
        ),
        image = this.image,
        episodes = this.episodes.split(",")
            .mapNotNull { it.trim().toIntOrNull() },
        isFavorite = true
    )
}

fun AllCharacterDomainModel.toFavoriteEntity(): FavoriteCharacterEntity {
    return FavoriteCharacterEntity(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        type = this.type,
        gender = this.gender,
        location = LocationEmbeddable(
            id = this.location.id,
            name = this.location.name
        ),
        origin = LocationEmbeddable(
            id = this.origin.id,
            name = this.origin.name
        ),
        image = this.image,
        episodes = this.episodes.joinToString(","),
        isFavorite = true
    )
}

fun List<FavoriteCharacterEntity>.toDomainList(): List<AllCharacterDomainModel> {
    return this.map { it.toDomain() }
}

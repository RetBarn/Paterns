package com.example.rickandmorty.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rickandmorty.domain.domainModel.AllCharacterDomainModel
import com.example.rickandmorty.domain.domainModel.CharacterLocationDomainModel
import com.example.rickandmorty.domain.domainModel.CharactersGender
import com.example.rickandmorty.domain.domainModel.CharactersStatus

@Entity("characters")
data class CharacterEntity(
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

    @ColumnInfo("image")
    val image: String,

    @ColumnInfo("isFavorite")
    val isFavorite: Boolean = false
)

fun CharacterEntity.toDomain(): AllCharacterDomainModel {
    return AllCharacterDomainModel(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        type = this.type,
        gender = this.gender,
        location = CharacterLocationDomainModel(-1, ""),
        origin = CharacterLocationDomainModel(-1, ""),
        image = this.image,
        episodes = emptyList(),
        isFavorite = this.isFavorite
    )
}

fun AllCharacterDomainModel.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        type = this.type,
        gender = this.gender,
        image = this.image,
        isFavorite = this.isFavorite
    )
}

fun List<CharacterEntity>.toDomainList(): List<AllCharacterDomainModel> {
    return this.map { it.toDomain() }
}

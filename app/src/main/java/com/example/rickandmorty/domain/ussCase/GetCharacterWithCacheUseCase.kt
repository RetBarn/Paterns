package com.example.rickandmorty.domain.ussCase

import com.example.rickandmorty.domain.domainModel.PageInfoDomainModel
import com.example.rickandmorty.domain.domainModel.ResponseDomainModel
import com.example.rickandmorty.domain.domainModel.ResultModel
import okhttp3.Response
import javax.inject.Inject

class GetCharacterWithCacheUseCase @Inject constructor(
    private val getCachedCharacters: GetCachedCharactersUseCase,
    private val getCharacters: GetCharactersUseCase,
    private val saveCharacters: SaveCharactersUseCase,
    private val clearCache: ClearCacheUseCase,
    private val getFavorites: GetFavoritesUseCase
) {
    suspend operator fun invoke(page: Int = 1): ResultModel<ResponseDomainModel> {
        if (page == 1) {
            val cachedResult = getCachedCharacters(page)
            if (cachedResult is ResultModel.Success && cachedResult.data.isNotEmpty()) {
                return ResultModel.Success(
                    ResponseDomainModel(
                        info = PageInfoDomainModel(
                            count = cachedResult.data.size,
                            pages = 1,
                            next = if (cachedResult.data.size == 20) "2" else null,
                            prev = null
                        ),
                        result = cachedResult.data
                    )
                )
            }
        }

        val networkResult = getCharacters(page)

        if (networkResult is ResultModel.Success && page == 1) {
            clearCache()
            saveCharacters(networkResult.data.result)
        }

        return networkResult
    }
}
package com.northbay.rag_chat_storage.dto;

import lombok.Data;

@Data
public class FavoriteSessionRequest {
    private boolean favorite;

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
}
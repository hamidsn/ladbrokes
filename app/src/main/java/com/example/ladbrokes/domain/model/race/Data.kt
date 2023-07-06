package com.example.ladbrokes.domain.model.race

import com.google.gson.internal.LinkedTreeMap

data class Data(
    val next_to_go_ids: List<String>,
    val race_summaries: LinkedTreeMap<String, SubData>
)
package br.com.angelorobson.alternativescene.application.partials.events.create

import ir.mirrajabi.searchdialog.core.Searchable

class SearchModel(private var mTitle: String) : Searchable {

    override fun getTitle(): String {
        return mTitle
    }

}
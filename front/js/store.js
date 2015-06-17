import request from 'browser-request'
import "babel/polyfill"
import 'whatwg-fetch'

class Store {
  // Ref: app/models/RSS.scala

  constructor() {
    this.data = {
      categories: [],
      feed: {} // cgid: Feed
    }
  }

  fetchAll() {
    var self = this
    return this.fetchCategories().then(cgs => {
      for (let category of cgs) {
        self.fetchFeed(category.cgid)
      }
      return self.data
    })
  }

  fetchCategories() {
    var self = this
    return fetch('/api/categories').then(response =>
      response.json().then(json => {
        self.data.categories = json
        return json
      })
    )
  }

  fetchFeed(cgid) {
    var self = this
    return fetch(`/api/feed/${cgid}`).then(response =>
      response.json().then(json => {
        self.data.feed[cgid] = json
        return json
      })
    )
  }

}

export default new Store

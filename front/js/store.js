import request from 'browser-request'
import "babel/polyfill"
import 'whatwg-fetch'
import _ from 'lodash'

class Store {
  // Ref: app/models/RSS.scala

  constructor() {
    this.data = {
      categories: [],
      feed: {}, // cgid: Feed
      articles: []
    }
  }

  fetchAll() {
    var self = this
    return this.fetchCategories().then(cgs => {
      return Promise.all(cgs.map(cg => self.fetchFeed(cg.cgid))).then(_ => self.data)
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
        self.data.articles.concat(json.articles)
        return json
      })
    )
  }

  getArticles() {
    var self = this
    if (self.data.articles.length > 0)
      return new Promise(_ => self.data.articles)
    else return this.fetchAll().then(data => {
      return _.flatten(_.values(data.feed).map(feed => feed.articles))
    })
  }

  findArticle(guid) {
    return this.getArticles().then(articles =>
      articles.find(article => article.guid === guid)
    )
  }

}

export default new Store

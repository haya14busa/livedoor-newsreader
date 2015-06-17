import request from 'browser-request'
import "babel/polyfill"
import 'whatwg-fetch'
import _ from 'lodash'

function returnAsPromise(data) {
  return new Promise((resolve, reject) => resolve(data))
}

class Store {
  // Ref: app/models/RSS.scala

  constructor() {
    this.data = {
      categories: [],
      feed: {}, // cgid: Feed
      articles: []
    }
  }

  getAll() {
    var self = this
    return this.getCategories().then(cgs => {
      return Promise.all(cgs.map(cg => self.getFeed(cg.cgid))).then(_ => self.data)
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

  getCategories() {
    if (this.data.categories.length > 0)
      return returnAsPromise(this.data.categories)
    else return this.fetchCategories()
  }

  fetchFeed(cgid) {
    var self = this
    return fetch(`/api/feed/${cgid}`).then(response =>
      response.json().then(json => {
        self.data.feed[cgid] = json
        self.data.articles = self.data.articles.concat(json.articles)
        return json
      })
    )
  }

  getFeed(cgid) {
    if (cgid in this.data.feed)
      return returnAsPromise(this.data.feed[cgid])
    else
      return this.fetchFeed(cgid)
  }

  getArticles() {
    if (this.data.articles.length > 0)
      return returnAsPromise(this.data.articles)
    else return this.getAll().then(data => {
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

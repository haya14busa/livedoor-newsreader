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
      articles: [],
      relatedArticles: {} // guid: List[RelatedArticle]
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

  findArticle(guid) {
    var r = this.data.articles.find(article => article.guid === guid)
    return r ? returnAsPromise(r) : fetch(`/api/article/${guid}`).then(response =>
      response.json().then(json => {
        this.data.articles = this.data.articles.concat(json)
        return json
      })
    )
  }

  findRelatedArticle(guid) {
    return (guid in this.data.relatedArticles)
      ? this.data.relatedArticles[guid]
      : fetch(`/api/article/${guid}/related`).then(response =>
        response.json().then(json => {
        this.data.relatedArticles[guid] = json
        return json
      })
    )
  }

}

export default new Store

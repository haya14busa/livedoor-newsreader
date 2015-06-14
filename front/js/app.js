class Cat {

  constructor(name) {
    this.name = name
  }

  meow() {
    alert( this.name + 'はミャオと鳴きました' )
  }

}

var cat = new Cat('tama')
cat.meow()

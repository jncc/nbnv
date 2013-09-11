define [
  "cs!helpers/representations/Styles"
], (Styles) ->
  describe "Styling Helper", ->
    it "can shrink red full opacity style", ->
      shrunk = Styles.shrinkStyle colour: "ff0000", opacity: 1

      expect(shrunk).toBe "/m00/"

    it "can expand red full opacity style", ->
      style = Styles.expandStyle("/m00/")

      expect(style.colour).toBe "ff0000"
      expect(style.opacity).toBe 1

    it "can shrink green full opacity style", ->
      shrunk = Styles.shrinkStyle colour: "00ff00", opacity: 1

      expect(shrunk).toBe "Fy0/"

    it "can expand green full opacity style", ->
      style = Styles.expandStyle("Fy0/")

      expect(style.colour).toBe "00ff00"
      expect(style.opacity).toBe 1
    
    it "can shrink blue full opacity style", ->
      shrunk = Styles.shrinkStyle colour: "0000ff", opacity: 1

      expect(shrunk).toBe "3//"

    it "can expand blue full opacity style", ->
      style = Styles.expandStyle("3//")

      expect(style.colour).toBe "0000ff"
      expect(style.opacity).toBe 1

    it "can shrink blue full transparent style", ->
      shrunk = Styles.shrinkStyle colour: "0000ff", opacity: 0

      expect(shrunk).toBe "3/0"

    it "can expand blue full transparent style", ->
      style = Styles.expandStyle("3/0")

      expect(style.colour).toBe "0000ff"
      expect(Math.round style.opacity).toBe 0
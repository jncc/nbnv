define [
  "cs!helpers/Numbers"
], (Numbers)-> 
  describe "numbers helper", ->
    it "can pad a number which is too short", ->
      shortNum = 20
      padded = Numbers.pad shortNum, 6
      expect(padded).toBe("000020")

    it "can pad a number which is the correct length", ->
      number = 3721
      padded = Numbers.pad number, 4
      expect(padded).toBe("3721")

    it "doesn't pad a number which is already too long", ->
      number = 123123
      padded = Numbers.pad number, 2
      expect(padded).toBe("123123")

    it "can encode/decode numbers from 0 to 100000", ->
      for i in [0..100000]
        base64 = Numbers.toBase64 i
        number = Numbers.fromBase64 base64
        expect(i).toBe(number)
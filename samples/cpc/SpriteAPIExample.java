import net.mikekohn.java_grinder.CPC;
import net.mikekohn.java_grinder.Memory;

public class SpriteAPIExample
{
  static final int SCREEN_BASE = 0xC000;
  static final int OPAQUE_ADDRESS = 0x9000;
  static final int MASKED_ADDRESS = 0x9100;

  static final char SPRITE_WIDTH = 4;
  static final char SPRITE_HEIGHT = 8;

  /*
   * Four bytes per raster line.
   * The values are already encoded as CPC Mode 0 screen bytes.
   */
  static int OpaqueSprite[] =
  {
    0x00, 0xC0, 0xC0, 0x00,
    0xC0, 0xCC, 0xCC, 0xC0,
    0xC0, 0x3C, 0x3C, 0xC0,
    0xCC, 0x3C, 0x3C, 0xCC,
    0xCC, 0xFC, 0xFC, 0xCC,
    0xC0, 0xCC, 0xCC, 0xC0,
    0xC0, 0xC0, 0xC0, 0xC0,
    0x00, 0xC0, 0xC0, 0x00
  };

  /*
   * Masked data is stored as mask,sprite pairs.
   * 0xff,0x00 keeps the old screen byte.
   * 0x00,value replaces the old screen byte.
   */
  static int MaskedSprite[] =
  {
    0xFF,0x00, 0x00,0xC0, 0x00,0xC0, 0xFF,0x00,
    0x00,0xC0, 0x00,0xCC, 0x00,0xCC, 0x00,0xC0,
    0x00,0xC0, 0x00,0x3C, 0x00,0x3C, 0x00,0xC0,
    0x00,0xCC, 0x00,0x3C, 0x00,0x3C, 0x00,0xCC,
    0x00,0xCC, 0x00,0xFC, 0x00,0xFC, 0x00,0xCC,
    0x00,0xC0, 0x00,0xCC, 0x00,0xCC, 0x00,0xC0,
    0x00,0xC0, 0x00,0xC0, 0x00,0xC0, 0x00,0xC0,
    0xFF,0x00, 0x00,0xC0, 0x00,0xC0, 0xFF,0x00
  };

  public static void main(String[] args)
  {
    char i;
    char x;
    char y;
    short direction;
    int destination;
    int line;

    CPC.screen(0);
    CPC.setBorderColor(0);
    CPC.cls();

    /* Copy sprite data to fixed native CPC RAM addresses once. */
    for (i = 0; i < 32; i++)
    {
      Memory.write8(OPAQUE_ADDRESS + i, (byte)OpaqueSprite[i]);
    }

    for (i = 0; i < 64; i++)
    {
      Memory.write8(MASKED_ADDRESS + i, (byte)MaskedSprite[i]);
    }

    /*
     * Demonstrate nextScreenLine by drawing an eight-line background block.
     */
    line = CPC.screenAddress(SCREEN_BASE, 0, 80);

    for (i = 0; i < 8; i++)
    {
      Memory.write8(line, (byte)0xFF);
      Memory.write8(line + 1, (byte)0xFF);
      Memory.write8(line + 2, (byte)0xFF);
      Memory.write8(line + 3, (byte)0xFF);
      line = CPC.nextScreenLine(line);
    }

    /* Draw one opaque sprite at a fixed position. */
    destination = CPC.screenAddress(SCREEN_BASE, 8, 40);
    CPC.blitOpaque(destination, OPAQUE_ADDRESS, SPRITE_WIDTH, SPRITE_HEIGHT);

    x = 20;
    y = 80;
    direction = 1;

    while (true)
    {
      CPC.VSync();

      x += direction;

      if (x <= 0)
      {
        x = 0;
        direction = 1;
      }

      if (x >= 76)
      {
        x = 76;
        direction = -1;
      }

      destination = CPC.screenAddress(SCREEN_BASE, x, y);
      CPC.blitMasked(destination, MASKED_ADDRESS, SPRITE_WIDTH, SPRITE_HEIGHT);
    }
  }
}

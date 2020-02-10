package squares.api.block;

import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

import squares.api.block.Block;

public class BlockFactory {
	Map<Character, Function<String, Block>> blockProvs = new HashMap<>();

	public BlockFactory addProv(Character c, Function<String, Block> prov) {
		blockProvs.put(c, prov);
		return this;
	}

	public Block makeBlock(String input) {
		return blockProvs.get(input.charAt(0)).apply(input.substring(1));
	}
}

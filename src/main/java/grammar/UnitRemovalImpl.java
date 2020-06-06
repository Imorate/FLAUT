package grammar;

import grammar.interfaces.UnitRemoval;
import grammar.model.Grammar;
import lombok.Data;

@Data
public class UnitRemovalImpl implements UnitRemoval {
    private Grammar grammar;

    public UnitRemovalImpl(Grammar grammar) {
        this.grammar = grammar;
    }

    @Override
    public Grammar unitRemovalFix() {
        return this.grammar;
    }
}

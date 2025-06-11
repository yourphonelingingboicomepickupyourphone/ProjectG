package skill;

import java.util.ArrayList;
import java.util.List;

public class SkillTreeNode {
    public Skill skill;
    public List<SkillTreeNode> children = new ArrayList<>();
    public SkillTreeNode parent;
    public boolean unlocked = false;
    public int requiredPoints = 1; // Points needed to unlock

    public SkillTreeNode(Skill skill) {
        this.skill = skill;
    }
}

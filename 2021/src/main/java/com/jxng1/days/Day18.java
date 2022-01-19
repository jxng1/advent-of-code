package main.java.com.jxng1.days;

import java.util.List;

public class Day18 extends Day {
    public Day18(int day) {
        super(day);
    }

    @Override
    String task1(List<String> input) {
        Pair ret = parsePair(input.get(0));

        for (int i = 1; i < input.size(); i++) {
            ret = ret.append(parsePair(input.get(i)));
            while (ret.reduce()) {
                // System.out.println(ret.toString());
            }
        }

        return String.valueOf(ret.getValue());
    }

    @Override
    String task2(List<String> input) {
        return String.valueOf(input.stream().mapToInt(a ->
                input.stream().map(s -> {
                    if (!s.equals(a)) {
                        return parsePair(a).append(parsePair(s));
                    } else {
                        return null;
                    }
                }).map(pair -> {
                    if (pair != null) {
                        while (pair.reduce()) {
                            // System.out.println(ret.toString());
                        }

                        return pair.getValue();
                    }

                    return 0;
                }).max(Integer::compare).get()
        ).max().getAsInt());
    }

    private Pair parsePair(String input) {
        int openBrackets = 0;
        int commaIndex = 0;

        pairCharacterLoop:
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            switch (c) {
                case '[' -> openBrackets++;
                case ']' -> openBrackets--;
                case ',' -> {
                    if (openBrackets == 1) {
                        commaIndex = i;
                        break pairCharacterLoop;
                    }
                }
            }
        }

        String left = input.substring(1, commaIndex); // start index of 1 means we skip the opening bracket
        String right = input.substring(commaIndex + 1, input.length() - 1); // commandIndex + 1 means we skip the comma, input.length() - 1 means we don't go out of bounds
// try to do this with input.length() - 2 instead and modify parseInput...

        return new Pair(parseInput(left), parseInput(right));
    }

    private Pair parseInput(String input) {
        if (input.charAt(0) != '[') { // input parsed is a regular number
            return new Pair(Integer.parseInt(input));
        } else { // input parsed is another pair
            int endOfPair = findPairEndingIndex(input);
            String pairString = input.substring(0, endOfPair + 1); // as substring end index is exclusive

            return parsePair(pairString);
        }
    }

    private int findPairEndingIndex(String input) {
        int openBrackets = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '[') {
                openBrackets++;
            } else if (input.charAt(i) == ']') {
                openBrackets--;
            }

            if (openBrackets == 0) {
                return i;
            }
        }

        return -1;
    }

    class Pair {
        private Pair parent;
        private Pair left;
        private Pair right;
        private int value;

        public Pair(Pair left, Pair right) {
            this.left = left;
            if (left != null) {
                this.left.parent = this;
            }

            this.right = right;
            if (right != null) {
                this.right.parent = this;
            }
        }

        public Pair(int value) {
            this.left = null;
            this.right = null;
            this.value = value;
        }

        public Pair getRight() {
            return right;
        }

        public Pair getLeft() {
            return left;
        }

        public boolean isRegular() {
            return left == null && right == null;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getValue() {
            if (isRegular()) {
                return value;
            } else {
                return 3 * this.left.getValue() + 2 * this.right.getValue();
            }
        }

        public Pair append(Pair otherPair) {
            return new Pair(this, otherPair);
        }

        public boolean reduce() {
            return explode(0) || split();
        }

        private boolean explode(int depth) { // this pair only explodes if depth is 4, otherwise traverse to this pair's pair
            if (this.left != null) { // try and explode the left child
                if (this.left.explode(depth + 1)) {
                    return true;
                }
            }

            if (this.right != null) { // try and explode the right child
                if (this.right.explode(depth + 1)) {
                    return true;
                }
            }

            if (depth == 4 && !this.isRegular()) { // explode oneself
                Pair leftRegular = this.parent.findFirstRegularSnailfishToSide(true, this);
                Pair rightRegular = this.parent.findFirstRegularSnailfishToSide(false, this);

                if (leftRegular != null) {
                    leftRegular.value += this.left.value;
                }

                if (rightRegular != null) {
                    rightRegular.value += this.right.value;
                }

                // this node has become a regular node and it's children are gone
                this.left = null;
                this.right = null;
                this.value = 0;

                return true;
            }

            return false;
        }

        private boolean split() {
            if (this.left.isRegular()) { // the left node of this node is a regular
                if (this.left.value >= 10) {
                    this.left = new Pair(
                            new Pair(Math.floorDiv(this.left.value, 2)), // left
                            new Pair((int) Math.ceil(this.left.value / 2.0))); // right
                    this.left.parent = this; // need to enforce parent is oneself

                    return true;
                }
            } else { // the left node of this node is another pair, try and split it instead
                // once the split occurs, if it happens, return as we only do one action per reduction,
                // otherwise go to the right side
                if (this.left.split()) {
                    return true;
                }
            }

            if (this.right.isRegular()) {
                if (this.right.value >= 10) {
                    this.right = new Pair(
                            new Pair(Math.floorDiv(this.right.value, 2)), // left
                            new Pair((int) Math.ceil(this.right.value / 2.0))); // right
                    this.right.parent = this; // need to enforce parent is oneself

                    return true;
                }
            } else { // the right node of this node is another pair, try and split it instead
                // once the split occurs, if it happens, return as we only do one action per reduction,
                // otherwise split was unsuccessful
                if (this.right.split()) {
                    return true;
                }
            }

            return false;
        }

        private Pair findFirstRegularSnailfishToSide(boolean checkLeft, Pair requester) {
            // we have to find the closest left and right; we have 4 depths we can check...
            // note that if a pair has another side
            // e.g. [[1, [1, 2]], [[2, 3], 2]] (and we explode [1, 2]),
            // we must look left after crossing the side
            // and vice versa on right...
            if (checkLeft) {
                if (requester == this.left) {
                    if (this.parent == null) { // this is the root, can't look for anymore
                        return null;
                    }

                    return this.parent.findFirstRegularSnailfishToSide(true, this);
                } else {
                    if (this.left.isRegular()) { // left node is regular, found the closest left
                        return this.left;
                    } else { // we've reached the other side, so check right side
                        return this.left.getLeaf(false);
                    }
                }
            } else {
                if (requester == this.right) {
                    if (this.parent == null) { // this is the root, can't look for anymore
                        return null;
                    }

                    return this.parent.findFirstRegularSnailfishToSide(false, this);
                } else {
                    if (this.right.isRegular()) { // right node is regular, found the closest right
                        return this.right;
                    } else { // we've reached the other side, so check left side
                        return this.right.getLeaf(true);
                    }
                }
            }
        }

        private Pair getLeaf(boolean onLeft) {
            // traverses recursively to the leaf node of a pair
            if (onLeft) {
                if (this.left.isRegular()) {
                    return this.left;
                } else {
                    return this.left.getLeaf(true);
                }
            } else {
                if (this.right.isRegular()) {
                    return this.right;
                } else {
                    return this.right.getLeaf(false);
                }
            }
        }

        @Override
        public String toString() {
            if (isRegular()) { // if this node is regular, just return the value it has
                return String.valueOf(value);
            } else { // this node has pairs
                return String.format("[%s,%s]", this.left.toString(), this.right.toString());
            }
        }
    }
}

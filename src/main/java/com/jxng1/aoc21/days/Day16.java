package main.java.com.jxng1.aoc21.days;

import main.java.com.jxng1.Day;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Day16 extends Day {

    public Day16(int day) {
        super(day);
    }

    @Override
    protected String task1(List<String> input) {
        var binaryList = formBinaryString(input.get(0));
        Packet outerPacket = nextPacket(binaryList);

        return String.valueOf(sumOfPacketVersions(outerPacket, 0));
    }

    @Override
    protected String task2(List<String> input) {
        var binaryList = formBinaryString(input.get(0));
        Packet outerPacket = nextPacket(binaryList);

        calculateValueOfPacket(outerPacket);
        return String.valueOf(outerPacket.getLiteralValue());
    }

    private LinkedList<Character> formBinaryString(String transmission) {
        return Arrays.stream(transmission.split(""))
                .map(digit -> String.format("%1$" + 4 + "s", Integer.toBinaryString(Integer.parseInt(digit, 16))).replace(' ', '0'))
                .collect(Collectors.toList())
                .stream()
                .flatMapToInt(String::chars).mapToObj(e -> (char) e).collect(Collectors.toCollection(LinkedList::new));
    }

    private String takeFromCharacterList(LinkedList<Character> characters, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(characters.removeFirst());
        }

        return sb.toString();
    }

    private long calculateValueOfPacket(Packet packet) {
        switch (packet.getTypeID()) {
            case 0 -> { // sum packet - value is sum of subpackets
                long sum = packet.getSubPackets().stream().mapToLong(this::calculateValueOfPacket).sum();
                packet.setLiteralValue(sum);

                return sum;
            }
            case 1 -> { // product packet - value is product of subpackets
                long product = packet.getSubPackets().stream().mapToLong(this::calculateValueOfPacket).reduce((a, b) -> a * b).getAsLong();
                packet.setLiteralValue(product);

                return product;
            }
            case 2 -> { // minimum packet - value is minimum value of subpackets
                long min = packet.getSubPackets().stream().mapToLong(this::calculateValueOfPacket).min().getAsLong();
                packet.setLiteralValue(min);

                return min;
            }
            case 3 -> { // maximum packet - value is maximum value of subpackets
                long max = packet.getSubPackets().stream().mapToLong(this::calculateValueOfPacket).max().getAsLong();
                packet.setLiteralValue(max);

                return max;
            }
            case 4 -> { // literal packet
                return packet.getLiteralValue();
            }
            case 5 -> { // greater-than packet - value is 1 if the value of the first sub-packet is greater than the value of the second sub-packet; otherwise, their value is 0; only has two subpackets
                long ret = packet.getSubPackets().stream().mapToLong(this::calculateValueOfPacket).reduce((a, b) -> a > b ? 1L : 0L).getAsLong();
                packet.setLiteralValue(ret);

                return ret;
            }
            case 6 -> { // less-than packet - value is 1 if the value of the first sub-packet is less than the value of the second sub-packet; otherwise, their value is 0; only has two subpackets
                long ret = packet.getSubPackets().stream().mapToLong(this::calculateValueOfPacket).reduce((a, b) -> a < b ? 1L : 0L).getAsLong();
                packet.setLiteralValue(ret);

                return ret;
            }
            case 7 -> { // equal to - value is 1 if the value of the first sub-packet is equal to the value of the second sub-packet; otherwise, their value is 0; only has two subpackets
                long ret = packet.getSubPackets().stream().mapToLong(this::calculateValueOfPacket).reduce((a, b) -> a == b ? 1L : 0L).getAsLong();
                packet.setLiteralValue(ret);

                return ret;
            }
            default -> {
                return 0;
            }
        }
    }

    private Packet nextPacket(LinkedList<Character> binaryCharacters) {
        int version = Integer.parseInt(takeFromCharacterList(binaryCharacters, 3), 2);
        int typeID = Integer.parseInt(takeFromCharacterList(binaryCharacters, 3), 2);
        Packet packet = new Packet(version, typeID);

        if (typeID == 4) { // is a literal packet
            extractLiteralPackets(packet, binaryCharacters);
        } else { // is an operator
            extractOperatorPackets(packet, binaryCharacters);
        }

        return packet;
    }

    private void extractOperatorPackets(Packet packet, LinkedList<Character> characters) {
        String lengthTypeID = takeFromCharacterList(characters, 1); // either 1 or 0

        if (lengthTypeID.equals("0")) { // if 0, next 15 bits are a number that represents total length of subpackets
            int length = Integer.parseInt(takeFromCharacterList(characters, 15), 2);
            int limit = characters.size() - length;

            while (characters.size() > limit) {
                packet.addSubPacket(nextPacket(characters));
            }
        } else { // if 1, next 11 bits are number that represents number of subpackets contained within this packet
            int packetCount = Integer.parseInt(takeFromCharacterList(characters, 11), 2);
            for (int i = 0; i < packetCount; i++) {
                packet.addSubPacket(nextPacket(characters));
            }
        }
    }

    private void extractLiteralPackets(Packet packet, LinkedList<Character> characters) {
        StringBuilder sb = new StringBuilder();
        char first;
        do {
            first = takeFromCharacterList(characters, 1).charAt(0);
            sb.append(takeFromCharacterList(characters, 4));
        } while (first != '0');

        packet.setLiteralValue(Long.parseLong(sb.toString(), 2));
    }

    private long sumOfPacketVersions(Packet packet, long sum) {
        for (Packet subpacket : packet.getSubPackets()) {
            sum = sumOfPacketVersions(subpacket, sum);
        }
        return sum + packet.getVersion();
    }

    class Packet {
        private final int version;
        private final int typeID;
        private long literalValue;
        private final LinkedList<Packet> packets = new LinkedList<>();

        public Packet(int version, int typeID) {
            this.version = version;
            this.typeID = typeID;
            this.literalValue = 0;
        }

        public int getTypeID() {
            return typeID;
        }

        public int getVersion() {
            return version;
        }

        public void addSubPacket(Packet subpacket) {
            packets.add(subpacket);
        }

        public LinkedList<Packet> getSubPackets() {
            return packets;
        }

        public void setLiteralValue(long value) {
            literalValue = value;
        }

        public long getLiteralValue() {
            return literalValue;
        }
    }
}

package me.rafahop.aoc.puzzle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Puzzle04 implements Puzzle<Long> {

    List<Passport> passports;
    
    @Override
    public void init(Path file) throws Exception {
        List<String> lines = Files.lines(file).collect(Collectors.toList());
        passports = new ArrayList<>();
        Passport currentPassport = new Passport();
        for (String line : lines) {
            if (line.isEmpty()) {
                passports.add(currentPassport);
                currentPassport = new Passport();
            } else {
                currentPassport.addContent(line);
            }
        }
        passports.add(currentPassport);
    }

    @Override
    public Long part1() {
        return passports.stream().filter(Passport::hasAllRequiredFields).count();
    }

    @Override
    public Long part2() {
        return passports.stream().filter(Passport::isValid).count();
    }

}

enum EyeColour {
    AMB("amb"), BLU("blu"), BRN("brn"), GRY("gry"), GRN("grn"), HZL("hzl"), OTH("oth");

    private String value;

    private EyeColour(String value) {
        this.value = value;
    }
    
    static EyeColour getEyeColour(String c) {
        for (EyeColour e : EyeColour.values()) {
            if (e.value.equals(c)) {
                return e;
            }
        }
        return null;
    }
}

class Height {
    int value;
    String unit;
    
    Height(String height) {
        try {
            value = Integer.parseInt(height.substring(0, height.length() - 2));
        }catch (NumberFormatException e) {
            value = 0;
        }
        unit = height.substring(height.length() - 2, height.length());
    }
    
    boolean isValid() {
        return ("in".equals(unit) && value >= 59 && value <= 76) || ("cm".equals(unit) && value >= 150 && value <= 193);
    }
}

class Passport {
    private static final List<String> REQUIRED_FIELDS = Arrays.asList("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");
    private Map<String, String> content = new HashMap<>();
    
    private Integer birthYear;
    private Integer issueYear;
    private Integer expirationYear;
    private Height height;
    private String hairColour;
    private EyeColour eyeColour;
    private String passportId;
    private String countryId;
    
    public void addContent(String c) {
        
        Arrays.stream(c.split(" ")).map(s -> s.split(":")).forEach(a -> content.put(a[0], a[1]));
        if (content.containsKey("byr")) {
            try {
                birthYear = Integer.parseInt(content.get("byr"));
            } catch (NumberFormatException e) {
                birthYear = 0;
            }
        }

        if (content.containsKey("iyr")) {
            try {
                issueYear = Integer.parseInt(content.get("iyr"));
            } catch (NumberFormatException e) {
                issueYear = 0;
            }
        }

        if (content.containsKey("eyr")) {
            try {
                expirationYear = Integer.parseInt(content.get("eyr"));
            } catch (NumberFormatException e) {
                expirationYear = 0;
            }
        }

        if (content.containsKey("hgt")) {
            height = new Height(content.get("hgt"));
        }

        if (content.containsKey("hcl")) {
            hairColour = content.get("hcl");
        }

        if (content.containsKey("ecl")) {
            eyeColour = EyeColour.getEyeColour(content.get("ecl"));
        }

        if (content.containsKey("pid")) {
            passportId = content.get("pid");
        }

        if (content.containsKey("cid")) {
            countryId = content.get("cid");
        }
    }
    
    public boolean hasAllRequiredFields() {
        return content.keySet().containsAll(REQUIRED_FIELDS);
    }
    
    public boolean isValid() {
        boolean valid = hasAllRequiredFields();
        valid &= birthYear != null && birthYear >= 1920 && birthYear <= 2002;
        valid &= issueYear != null && issueYear >= 2010 && issueYear <= 2020;
        valid &= expirationYear!=null && expirationYear >= 2020 && expirationYear <= 2030;
        valid &= height != null && height.isValid();
        valid &= hairColour != null && hairColour.matches("#[0-9a-f]{6}");
        valid &= eyeColour != null;
        valid &= passportId != null && passportId.length() == 9;
        try {
            Long.parseLong(passportId);
        } catch (NumberFormatException e) {
            valid = false;
        }
        return valid;
    }
}
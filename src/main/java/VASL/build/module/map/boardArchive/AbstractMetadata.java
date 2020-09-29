package VASL.build.module.map.boardArchive;


import java.awt.Color;
import java.util.LinkedHashMap;

import org.jdom2.Element;
import org.jdom2.JDOMException;

/**
 * Contains metadata that is common to the shared and board metadata files
 */
public abstract class AbstractMetadata {

    protected static final String colorsElement = "colors";
    protected static final String colorElement = "color";
    protected static final String colorNameAttribute = "name";
    protected static final String colorRedAttribute = "red";
    protected static final String colorGreenAttribute = "green";
    protected static final String colorBlueAttribute = "blue";
    protected static final String colorTerrainAttribute = "terrain";
    protected static final String colorElevationAttribute = "elevation";

    protected static final String colorSSRulesElement = "colorSSRules";
    protected static final String colorSSRElement = "colorSSR";
    protected static final String colorSSRuleName = "name";
    protected static final String colorMapElement = "colorMap";
    protected static final String colorMapFromColorAttribute = "fromColor";
    protected static final String colorMapToColorAttribute = "toColor";

    protected static final String LOSSSRulesElement = "LOSSSRules";
    protected static final String LOSSSRuleElement = "LOSSSRule";
    protected static final String LOSSSRuleNameAttribute = "name";
    protected static final String LOSSSRuleTypeAttribute = "type";
    protected static final String LOSSSRuleFromValueAttribute = "fromValue";
    protected static final String LOSSSRuleToValueAttribute = "toValue";
    protected static final String LOSSSRuleTypeValues[] = { // valid LOS rule types
            "ignore", "customCode", "terrainMap", "elevationMap", "terrainToElevationMap", "elevationToTerrainMap"
    };

    protected static final String LOSCounterRulesElement = "LOSCounterRules";
    protected static final String smokeCounterElement = "smokeCounter";
    protected static final String OBACounterElement = "OBACounter";
    protected static final String terrainCounterElement = "terrainCounter";
    protected static final String wreckCounterElement = "wreckCounter";
    protected static final String ignoreCounterElement = "ignoreCounter";
    protected static final String LOSCounterRuleNameAttribute = "name";
    protected static final String LOSCounterRuleHindranceAttribute = "hindrance";
    protected static final String LOSCounterRuleHeightAttribute = "height";
    protected static final String LOSCounterRuleTerrainAttribute = "terrain";

    // Maps color names to board color object
    protected LinkedHashMap<String, BoardColor> boardColors = new LinkedHashMap<String, BoardColor>(100);

    // Maps a Color object to VASL color name
    protected LinkedHashMap<Color, String> colorToVASLColorName = new LinkedHashMap<Color, String>(100);

    // Maps rule name to the rule object
    protected LinkedHashMap<String, ColorSSRule> colorSSRules = new LinkedHashMap<String, ColorSSRule>(100);

    // List of LOS scenario-specific rules
    protected LinkedHashMap<String, LOSSSRule> LOSSSRules = new LinkedHashMap<String, LOSSSRule>(100);

    // Lists of the counter rules
    protected LinkedHashMap<String, LOSCounterRule> LOSCounterRules = new LinkedHashMap<String, LOSCounterRule>(30);

    /**
     * Assert the element has the given name otherwise throw an exception
     * @param element the element
     * @param elementName the element name
     * @throws org.jdom2.JDOMException
     */
    protected void assertElementName(Element element, String elementName) throws JDOMException {

        // make sure we have the right element
        if(!element.getName().equals(elementName)) {
            throw new JDOMException("Invalid element passed to an element parser: " + elementName);
        }
    }

    /**
     * Parses the colors element
     * @param element the colors element
     * @throws org.jdom2.JDOMException
     */
    protected void parseColors(Element element) throws JDOMException {

        parseColors(element, false);
    }

    /**
     * Parses the colors element, replacing any existing colors
     * @param element the colors element
     * @param replace replace existing colors?
     * @throws org.jdom2.JDOMException
    */
    protected void parseColors(Element element, boolean replace) throws JDOMException {

        // make sure we have the right element
        assertElementName(element, colorsElement);

        for(Element e: element.getChildren()) {

            // ignore any child elements that are not color elements
            if(e.getName().equals(colorElement)){

                // read the color attributes
                String name = e.getAttributeValue(colorNameAttribute);
                String terrain = e.getAttributeValue(colorTerrainAttribute);
                String elevation = e.getAttributeValue(colorElevationAttribute);

                // create and set the color
                Color color = new Color(
                        e.getAttribute(colorRedAttribute).getIntValue(),
                        e.getAttribute(colorGreenAttribute).getIntValue(),
                        e.getAttribute(colorBlueAttribute).getIntValue()
                );

                BoardColor boardColor = new BoardColor(name, color, terrain, elevation);

                // add the color to the list of VASL colors
                boardColors.put(name, boardColor);

                // replace existing?
                if(replace) {
                    colorToVASLColorName.put(color, name);
                }

                // if there are redundant colors (and there are) keep the first one in the list
                else if (!colorToVASLColorName.containsKey(color)) {

                    colorToVASLColorName.put(color, name);
                }
            }
        }
    }

    /**
     * Parses the LOS scenario-specific rules element
     * @param element the LOSSSRules element
     * @throws org.jdom2.JDOMException
     */
    protected void parseLOSSSRules(Element element) throws JDOMException {

        // make sure we have the right element
        assertElementName(element, LOSSSRulesElement);

        for(Element e: element.getChildren()) {

            // ignore any child elements that are not color elements
            if(e.getName().equals(LOSSSRuleElement)){

                // read the attributes
                String name = e.getAttributeValue(LOSSSRuleNameAttribute);
                String type = e.getAttributeValue(LOSSSRuleTypeAttribute);
                String fromValue = e.getAttributeValue(LOSSSRuleFromValueAttribute);
                String toValue = e.getAttributeValue(LOSSSRuleToValueAttribute);

                // make sure the type code is valid
                boolean validTypeCode = false;
                for(int x = 0; x < LOSSSRuleTypeValues.length && !validTypeCode; x++) {
                    if(LOSSSRuleTypeValues[x].equals(type)) {
                        validTypeCode = true;
                    }
                }

                if(!validTypeCode) {
                    throw new JDOMException("Invalid LOS scenario-specific rule type: " + name);
                }

                // create and store the rule
                LOSSSRule losssRule = new LOSSSRule(name, type, fromValue, toValue);
                LOSSSRules.put(name, losssRule);
            }
        }
    }

    /**
     * Parses the scenario-specific color rules element
     * @param element the colorSSRules element
     * @throws org.jdom2.JDOMException
     */
    protected void parseColorSSRules(Element element) throws JDOMException {

        // make sure we have the right element
        assertElementName(element, colorSSRulesElement);

        for(Element e: element.getChildren()) {

            ColorSSRule colorSSRule = new ColorSSRule();
            String name = e.getAttributeValue(colorSSRuleName);

            // ignore any child elements that are not colorSSRules
            if(e.getName().equals(colorSSRElement)) {

                // read all of the mappings
                for (Element map: e.getChildren()) {

                    // ignore any child element that is not a color map
                    if(map.getName().equals(colorMapElement)) {

                        String fromColor = map.getAttributeValue(colorMapFromColorAttribute);
                        String toColor = map.getAttributeValue(colorMapToColorAttribute);

                        colorSSRule.addColorMap(fromColor, toColor);
                    }
                }
            }

            // make sure there is at least one mapping
            if (colorSSRule.getColorMaps().size() < 1) {
                throw new JDOMException("colorSSRule " + name + " has no mappings");
            }

            // save the rule
            colorSSRules.put(name, colorSSRule);
        }
    }

    /**
     * Parses the LOS counter rules element
     * @param element the LOSCounterRules element
     * @throws org.jdom2.JDOMException
     */
    protected void parseLOSCounterRules(Element element) throws JDOMException {

        // make sure we have the right element
        assertElementName(element, LOSCounterRulesElement);

        for(Element e: element.getChildren()) {

            LOSCounterRule losCounterRule = null;
            String name = e.getAttributeValue(LOSCounterRuleNameAttribute);

            // ignore any child elements that are not counter rules
            if(e.getName().equals(smokeCounterElement)) {

                // read the height and hindrance
                losCounterRule = new LOSCounterRule(name, LOSCounterRule.CounterType.SMOKE);
                losCounterRule.setHeight(e.getAttribute(LOSCounterRuleHeightAttribute).getIntValue());
                losCounterRule.setHindrance(e.getAttribute(LOSCounterRuleHindranceAttribute).getIntValue());

            }
            else if(e.getName().equals(terrainCounterElement)) {
                losCounterRule = new LOSCounterRule(name, LOSCounterRule.CounterType.TERRAIN);
                losCounterRule.setTerrain(e.getAttributeValue(LOSCounterRuleTerrainAttribute));

            }
            else if(e.getName().equals(OBACounterElement)) {
                losCounterRule = new LOSCounterRule(name, LOSCounterRule.CounterType.OBA);

            }
            else if(e.getName().equals(wreckCounterElement)) {
                losCounterRule = new LOSCounterRule(name, LOSCounterRule.CounterType.WRECK);

            }
            else if(e.getName().equals(ignoreCounterElement)) {
                losCounterRule = new LOSCounterRule(name, LOSCounterRule.CounterType.IGNORE);
            }

            LOSCounterRules.put(name, losCounterRule);
        }
    }

    /**
     * @return the board colors
     */
    protected LinkedHashMap<String, BoardColor> getBoardColors() {
        return boardColors;
    }

    /**
      * @return the color SSR rules
     */
    protected LinkedHashMap<String, ColorSSRule> getColorSSRules() {
        return colorSSRules;
    }

    /**
     * @return the color to VASL color name mapping
     */
    public LinkedHashMap<Color, String> getColorToVASLColorName() {
        return colorToVASLColorName;
    }

    /**
     * @return the list of LOS scenario-specific rules
     */
    public LinkedHashMap<String, LOSSSRule> getLOSSSRules(){
        return LOSSSRules;
    }

    /**
     * @return the list of LOS counter rules
     */
    public LinkedHashMap<String, LOSCounterRule> getLOSCounterRules(){
        return LOSCounterRules;
    }
}

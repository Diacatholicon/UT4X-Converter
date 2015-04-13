package org.xtx.ut4converter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.xtx.ut4converter.UTGames.UTGame;
import org.xtx.ut4converter.config.UserConfig;
import org.xtx.ut4converter.t3d.T3DLevelConvertor;
import org.xtx.ut4converter.t3d.T3DMatch;

/**
 * 
 * @author XtremeXp
 */
public class MapConverter {
    
    /**
     * UT Game the map will be converted from
     */
    UTGame inputGame;
    
    /**
     * UT Game the map will be converted to
     */
    UTGame outputGame;
    
    /**
     * Input map. Can be either a map (.unr, ...) or unreal text map (.t3d)
     */
    File inMap;
    
    /**
     * Final map name, might differ from original one.
     * E.G: AS-Mazon (UT99) -> AS-Mazon-Original (for UT4 for exemple)
     */
    String outMapName;
    
    File inT3d, outT3d;
    
    /**
     * Where all converted stuff will be converted
     */
    Path outPath;
    
    /**
     * Scale factor applied to converted level.
     */
    Double scale;
    
    /**
     * Quick converter of actor name.
     * E.G:
     * "Mover" (U1/UT99) -> "InterpActor" (UT3)
     */
    T3DMatch tm = new T3DMatch();
    
    /**
     * Tells whether or not map is team based
     */
    private Boolean isTeamGameType;
    
    SupU1UT99ToUT4Classes supportedActorClasses;
    
    T3DLevelConvertor t3dLvlConvertor;
    
    
    /**
     * Global config about where games are
     */
    UserConfig config;

    /**
     *
     * @return
     */
    public UTGame getInputGame() {
        return inputGame;
    }

    /**
     *
     * @return
     */
    public UTGame getOutputGame() {
        return outputGame;
    }

    /**
     *
     * @return
     */
    public File getInMap() {
        return inMap;
    }

    /**
     *
     * @return
     */
    public Double getScale() {
        return scale;
    }

    
    /**
     * 
     * @param inputGame Input game the map originally comes from
     * @param outputGame Output game the map will be converted to
     * @param inpMap Map to be converted (either a t3d file or map)
     * @param scale  Scale applied to level when converting( defaut = 1)
     */
    public MapConverter(UTGame inputGame, UTGame outputGame, File inpMap, Double scale) {
        this.inputGame = inputGame;
        this.outputGame = outputGame;
        this.inMap = inpMap;
        this.scale = scale;
        init();
    }
    
    /**
     * Indicates that gametype is team based
     * @return true is gametype is team based
     */
    public Boolean isTeamGameType() {
        return isTeamGameType;
    }

    /**
     *
     * @param isTeamGameType
     */
    public void setIsTeamGameType(Boolean isTeamGameType) {
        this.isTeamGameType = isTeamGameType;
    }
    
    /**
     * Tried to find property converted to some other game ...
     * @param name
     * @param withT3dClass
     * @param properties
     * @return 
     */
    public T3DMatch.Match getMatchFor(String name, boolean withT3dClass, Map<String, String> properties){
        return tm.getMatchFor(name, inputGame, outputGame, withT3dClass, properties);
    }
    
    /**
     *
     * @return
     */
    public HashMap<String, T3DMatch.Match> getActorClassMatch(){
        return tm.getActorClassMatch(inputGame, outputGame);
    }
    
    private void init(){
        
        //config = Configuration.getInstance();
        
        if(inMap.getName().endsWith(".t3d")){
            inT3d = inMap;
        }
        
        if(isTeamGameType == null){
            isTeamGameType = UTGameTypes.isTeamBasedFromMapName(inT3d != null ? inT3d.getName() : inMap.getName());
        }
        

        if(outMapName==null){
            outMapName = inT3d.getName().split("\\.")[0] + "-" + inputGame.shortName + "-" + outputGame.shortName;
        }
        
        supportedActorClasses = new SupU1UT99ToUT4Classes(this);

    }
    
    /**
     * Converts level
     * @param path Where all converted stuff is copied to
     * @throws Exception 
     */
    public void convertTo(String path) throws Exception {
        
        outPath = Paths.get(path);
        
        if(!outPath.toFile().exists()){
            outPath.toFile().mkdirs();
        }
        
        // t3d ever exported or directly converting from t3d file, then skip export of it 
        // and directly convert it
        if(inT3d != null){
            outT3d = new File(path + File.separator + outMapName + ".t3d");
            t3dLvlConvertor = new T3DLevelConvertor(inT3d, outT3d, this);
            t3dLvlConvertor.convert();
        }
        
        // TODO export t3d map
        else {
            throw new UnsupportedOperationException();
        }

    }

    /**
     *
     * @return
     */
    public SupportedClasses getSupportedActorClasses() {
        return supportedActorClasses;
    }

    /**
     *
     * @return
     */
    public T3DLevelConvertor getT3dLvlConvertor() {
        return t3dLvlConvertor;
    }

    /**
     *
     * @param t3dLvlConvertor
     */
    public void setT3dLvlConvertor(T3DLevelConvertor t3dLvlConvertor) {
        this.t3dLvlConvertor = t3dLvlConvertor;
    }

    public String getOutMapName() {
        return outMapName;
    }

    
    /**
     * Current user configuration such as program path for UT99 and so on ...
     * @return 
     */
    public UserConfig getConfig() {
        return config;
    }
    
    /**
     * 
     * @return <code>true</code> if the output game is using unreal engine 4
     */
    public boolean toUnrealEngine4(){
        return UTGames.isUnrealEngine4(this.getOutputGame());
    }
    
    /**
     *
     * @return
     */
    public UTGames.UnrealEngine getUnrealEngineTo(){
        return this.getOutputGame().engine;
    }
    
    /**
     * Indicated if converting from UT using Unreal Engine 1 or Unreal Engine 2
     * (basically Unreal1, UT99, Unreal 2, UT2003 and UT2004)
     * @return true if converting from Unreal Engine 1 or 2 UTx game
     */
    public boolean fromUE1OrUE2(){
        return UTGames.isUnrealEngine1(this.getInputGame()) || UTGames.isUnrealEngine2(this.getInputGame());
    }
    
    /**
     * Indicated if converting to UT using Unreal Engine 1 or Unreal Engine 2
     * (basically Unreal1, UT99, Unreal 2, UT2003 and UT2004)
     * @return true if converting to Unreal Engine 1 or 2 UTx game
     */
    public boolean toUE1OrUE2(){
        return UTGames.isUnrealEngine1(this.getOutputGame()) || UTGames.isUnrealEngine2(this.getOutputGame());
    }
    
    /**
     * Indicates games being converted from some UT game in unreal engine 3 or 4
     * @return 
     */
    public boolean fromUE3OrUE4(){
        return UTGames.isUnrealEngine3(this.getInputGame()) || UTGames.isUnrealEngine4(this.getInputGame());
    }
    
    /**
     * Indicates games being converted to some UT game in unreal engine 3 or 4
     * @return 
     */
    public boolean toUE3OrUE4(){
        return UTGames.isUnrealEngine3(this.getOutputGame()) || UTGames.isUnrealEngine4(this.getOutputGame());
    }
    
    /**
     * Tells if converting UT game using Unreal Engine 1 or 2
     * is being converted to some other UT game using Unreal Engine 3 or 4.
     * 
     * @return true if converting UT game using Unreal Engine 1 or 2 to UT game using Unreal Engine 3 or 4
     */
    public boolean isFromUE1UE2ToUE3UE4(){
        return fromUE1OrUE2() && toUE3OrUE4();
    }
    
    /**
     * Tells if converting UT game using Unreal Engine 1 or 2
     * is being converted to some other UT game using Unreal Engine 3 or 4.
     * 
     * @return true if converting UT game using Unreal Engine 1 or 2 to UT game using Unreal Engine 3 or 4
     */
    public boolean isFromUE3UE4ToUE1UE2(){
        return toUE1OrUE2() && fromUE3OrUE4();
    }

    public File getOutT3d() {
        return outT3d;
    }
    
    
}
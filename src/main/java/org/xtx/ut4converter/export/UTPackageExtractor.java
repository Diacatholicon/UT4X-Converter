/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtx.ut4converter.export;

import java.io.File;
import java.util.Set;
import java.util.logging.Logger;
import org.xtx.ut4converter.MapConverter;
import org.xtx.ut4converter.t3d.T3DRessource;
import org.xtx.ut4converter.tools.Installation;
import org.xtx.ut4converter.ucore.UPackageRessource;

/**
 * Base class for exporting stuff from Unreal Packages (including levels)
 * such as Textures, Sounds, StaticMeshes and so on.
 * @author XtremeXp
 */
public abstract class UTPackageExtractor {
    
    /**
     * Map converter
     */
    protected MapConverter mapConverter;
    
    
    /**
     * Temporary logger until we embed one in MapConverter class
     */
    public Logger logger;
    
    /**
     * 
     * @param mapConverter Map converter 
     */
    public UTPackageExtractor(MapConverter mapConverter) {
        this.mapConverter = mapConverter;
        this.logger = mapConverter.getLogger();
    }
    

    
    /**
     * Tells where to export files.
     * Basically: <programfolder>/Converted/<mapname>/<ressourcetype> (better package)
     * @param type Type of ressource to export
     * @return 
     */
    protected File getExportFolder(T3DRessource.Type type){
        File programFolder = Installation.getProgramFolder();
        String mapName = mapConverter.getInMap().getName();
        
        return new File(programFolder.getAbsolutePath() + File.separator + MapConverter.CONV_PATH + File.separator + mapName.split("\\.")[0] + File.separator + type.name() + File.separator);
    }
    
    
    /**
     * Extract ressource, generally some package that contains multiple files (ressources)
     * @param ressource
     * @return List of files exported
     * @throws java.lang.Exception If anythings goes wrong when exporting this ressource
     */
    public abstract Set<File> extract(UPackageRessource ressource) throws Exception;
    
    public abstract File getExporterPath();
    
    /**
     * Returns and start an instance of an extractor.
     * This depends of unreal engine version as well as game.
     * @param mapConverter
     * @param ressource
     * @return 
     */
    public static UTPackageExtractor getExtractor(MapConverter mapConverter, UPackageRessource ressource){
        
        if(mapConverter.packageExtractor != null){
            return mapConverter.packageExtractor;
        } else {
            // TODO handle for multiple extractors
            mapConverter.packageExtractor = UCCExporter.getInstance(mapConverter);
            return mapConverter.packageExtractor;
        }
    }
    
}

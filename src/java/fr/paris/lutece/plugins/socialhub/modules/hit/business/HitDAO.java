/*
 * Copyright (c) 2002-2012, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.socialhub.modules.hit.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 *
 * InteractionHitDAO
 *
 */
public class HitDAO implements IHitDAO
{
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_hit ) FROM socialhub_extender_hit ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO socialhub_extender_hit (id_hit, id_resource, resource_type, nb_hits) VALUES ( ?,?,?,? ) ";
    private static final String SQL_QUERY_UPDATE = " UPDATE socialhub_extender_hit SET id_resource = ?, resource_type = ?, nb_hits = ? WHERE id_hit = ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM socialhub_extender_hit WHERE id_hit = ? ";
    private static final String SQL_QUERY_SELECT_ALL = " SELECT id_hit, id_resource, resource_type, nb_hits FROM socialhub_extender_hit ";
    private static final String SQL_QUERY_SELECT = SQL_QUERY_SELECT_ALL + " WHERE id_hit = ? ";
    private static final String SQL_QUERY_SELECT_BY_PARAMETERS = SQL_QUERY_SELECT_ALL +
        " WHERE id_resource = ? AND resource_type = ? ";

    /**
     * New primary key
     * @param plugin the plugin
     * @return a new primary key
     */
    private int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey = 1;

        if ( daoUtil.next(  ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free(  );

        return nKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insert( Hit hit, Plugin plugin )
    {
        hit.setIdHit( newPrimaryKey( plugin ) );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        int nIndex = 1;

        daoUtil.setInt( nIndex++, hit.getIdHit(  ) );
        daoUtil.setString( nIndex++, hit.getIdExtendableResource(  ) );
        daoUtil.setString( nIndex++, hit.getExtendableResourceType(  ) );
        daoUtil.setInt( nIndex, hit.getNbHits(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( Hit hit, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;

        daoUtil.setString( nIndex++, hit.getIdExtendableResource(  ) );
        daoUtil.setString( nIndex++, hit.getExtendableResourceType(  ) );
        daoUtil.setInt( nIndex++, hit.getNbHits(  ) );

        daoUtil.setInt( nIndex, hit.getIdHit(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdHit, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );

        daoUtil.setInt( 1, nIdHit );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Hit load( int nIdHit, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nIdHit );
        daoUtil.executeQuery(  );

        Hit hit = null;

        int nIndex = 1;

        if ( daoUtil.next(  ) )
        {
            hit = new Hit(  );
            hit.setIdHit( daoUtil.getInt( nIndex++ ) );
            hit.setIdExtendableResource( daoUtil.getString( nIndex++ ) );
            hit.setExtendableResourceType( daoUtil.getString( nIndex++ ) );
            hit.setNbHits( daoUtil.getInt( nIndex ) );
        }

        daoUtil.free(  );

        return hit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Hit loadByParameters( String strIdExtendableResource, String strExtendableResourceType, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_PARAMETERS, plugin );
        int nIndex = 1;
        daoUtil.setString( nIndex++, strIdExtendableResource );
        daoUtil.setString( nIndex, strExtendableResourceType );
        daoUtil.executeQuery(  );

        Hit hit = null;

        if ( daoUtil.next(  ) )
        {
            nIndex = 1;
            hit = new Hit(  );
            hit.setIdHit( daoUtil.getInt( nIndex++ ) );
            hit.setIdExtendableResource( daoUtil.getString( nIndex++ ) );
            hit.setExtendableResourceType( daoUtil.getString( nIndex++ ) );
            hit.setNbHits( daoUtil.getInt( nIndex ) );
        }

        daoUtil.free(  );

        return hit;
    }
}
